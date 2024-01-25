const productUrl = "http://localhost:8080/products";

window.onload = () => {
  getProductItem();
  getReviews();
};

function goToHome() {
  window.location.href = "home.html";
}

function getProductItem() {
  const urlParams = new URLSearchParams(window.location.search);
  const productId = urlParams.get("id");
  const url = `${productUrl}/${productId}`;

  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createProductItem(jsonData);
    });
}

function createProductItem(data) {
  const container = document.getElementById("container");
  const img = document.getElementById("item-img");
  img.src = "../img/product/" + data.mainImgDto.filename;

  const subContainer = document.getElementById("sub-container");
  const name = document.getElementById("item-name");
  name.textContent = data.name;

  const price = document.getElementById("item-price");
  price.textContent = Number(data.price).toLocaleString() + " 원";

  const quantity = document.getElementById("item-quantity");
  const cartButton = document.querySelector(".button-cart");
  const orderButton = document.querySelector(".button-order");

  subContainer.appendChild(name);
  subContainer.appendChild(price);
  subContainer.appendChild(quantity);
  subContainer.appendChild(cartButton);
  subContainer.appendChild(orderButton);
  subContainer.appendChild(createProductImg(data.productImgDtoList, img));
  container.appendChild(subContainer);
}

function createProductImg(imgList, img) {
  const tableElement = document.getElementById("img-container");
  const maxImgPerRow = 5;
  var trElemet;

  imgList.forEach((item, index) => {
    if ((index + 1) % maxImgPerRow === 1) {
      trElemet = document.createElement("tr");
    }
    const tdElement = document.createElement("td");
    const imgElement = document.createElement("img");
    const imgPath = "../img/product/" + item.filename;
    imgElement.src = imgPath;
    imgElement.className = "detail-img";
    imgElement.addEventListener("click", () => {
      img.src = imgPath;
    });
    tdElement.appendChild(imgElement);
    trElemet.appendChild(tdElement);

    if ((index + 1) % maxImgPerRow === 0 || index === imgList.length - 1) {
      tableElement.appendChild(trElemet);
    }
  });
  return tableElement;
}
