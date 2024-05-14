const button = document.getElementById('read-hackernews');

button.addEventListener('click', async _ => {
  try {     
    const response = await fetch('/todos/hn', {
      method: 'post'
    });
    console.log('Completed!', response);
    setTimeout(() => {
        document.location.reload();
    }, 1000);
  } catch(err) {
    console.error(`Error: ${err}`);
  }
});
