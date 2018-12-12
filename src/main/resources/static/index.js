async function init() {

  const registration = await navigator.serviceWorker.register('/sw.js');
  await navigator.serviceWorker.ready;	
  firebase.initializeApp({
	    messagingSenderId: "239109235745"
  });
  const messaging = firebase.messaging();
  messaging.usePublicVapidKey('BLP0ZUfVCjieVtycpjejBm9GotBCgZ9c40yTufJbSlmA84IISlJ_cuo3h-h09Dd8FXNdrWA-0vpE0CujRwEn3ss');

  messaging.useServiceWorker(registration);	
  
  try {
    await messaging.requestPermission();
  } catch (e) {
    console.log('Unable to get permission', e);
    return;
  }

  navigator.serviceWorker.addEventListener('message', event => {
    if (event.data === 'newData') {
      showData();
    }
  });

  const currentToken = await messaging.getToken();
  fetch('/register', { method: 'post', body: currentToken });
  showData();

  messaging.onTokenRefresh(async () => {
    console.log('token refreshed');
    const newToken = await messaging.getToken();
    fetch('/register', { method: 'post', body: currentToken });
  });
  
}

async function showData() {
  const db = await getDb();
  const tx = db.transaction('messages', 'readonly');
  const store = tx.objectStore('messages');
  store.getAll().onsuccess = e => showMessages(e.target.result);
}

function showMessages(messages) {
  const table = document.getElementById('outTable');

  messages.sort((a, b) => parseInt(b.ts) - parseInt(a.ts));
  const html = [];
  messages.forEach(j => {
    const date = new Date(parseInt(j.ts));
    html.push(`<div><div class="header">${date.toISOString()} ${j.id} (${j.seq})</div><div class="message">${j.msg}</div></div>`);
  });
  table.innerHTML = html.join('');
}

async function getDb() {
  if (this.db) {
    return Promise.resolve(this.db);
  }
  return new Promise(resolve => {
    const openRequest = indexedDB.open("DietCi2", 1);

    openRequest.onupgradeneeded = event => {
      const db = event.target.result;
      db.createObjectStore('messages', { keyPath: 'id' });
    };

    openRequest.onsuccess = event => {
      this.db = event.target.result;
      resolve(this.db);
    }
  });
}

init();
