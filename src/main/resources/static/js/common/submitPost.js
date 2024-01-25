function postData(url, data, successCallback, errorCallback) {
  const host = "http://localhost:8080";
  fetch(host + url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      if (data.success) {
        successCallback(data);
      } else {
        console.error("request fail:", data.message);
        errorCallback(data.message);
      }
    })
    .catch((error) => console.error("Error:", error));
}
export { postData };
