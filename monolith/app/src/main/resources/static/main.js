(function main() {

  const statusEl = document.querySelector('#status');

  function setStatus(status) {
    statusEl.innerHTML = status;
  }

  setStatus('connecting...');

  const messagesEl = document.querySelector('#messages');

  function adddMessage({ data }) {
    const message = JSON.parse(data);
    const messageEl = document.createElement('li');
    messageEl.innerHTML = `
      <li>
        <p>
          <div>
            <span>from: ${message.from || 'nobody'}</span>
            <span>to: ${message.to || 'nobody'}</span> 
          </div> 
          <div>subject: ${message.subject || 'empty subject'}</div> 
          <div>body: ${message.body || 'empty body'}</div>
        </p> 
      </li>
    `;
    messagesEl.insertBefore(messageEl, messagesEl.firstChild);
  }

  const eventSource = new EventSource('/api/v1/messages', {
    headers: { 'type': 'application/stream+json' }
  });

  eventSource.onopen = e => setStatus('connection opened.');

  eventSource.onerror = function (e) {
    if (eventSource) eventSource.close();
    setStatus('connection closed.');
  };

  eventSource.onmessage = e => {
    setStatus('receiving message...');
    adddMessage(e);
  };

})();
