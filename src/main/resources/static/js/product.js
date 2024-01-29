const commonUrl = "http://localhost:8080/";
let productId = 0;

window.onload = () => {
  getProductItem();
  getReviews();
};

function goToHome() {
  window.location.href = "home.html";
}

function getProductItem() {
  const urlParams = new URLSearchParams(window.location.search);
  productId = urlParams.get("id");
  const productUrl = commonUrl + "products";
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
  var trElement;

  imgList.forEach((item, index) => {
    if ((index + 1) % maxImgPerRow === 1) {
      trElement = document.createElement("tr");
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
    trElement.appendChild(tdElement);

    if ((index + 1) % maxImgPerRow === 0 || index === imgList.length - 1) {
      tableElement.appendChild(trElement);
    }
  });
  return tableElement;
}

function addCart() {
  var cartUrl = commonUrl + "carts";
  var quantity = document.getElementById("input-quantity").value;
  var data = {
    productId: productId,
    quantity: quantity,
  };
  fetch(cartUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      if (jsonData.status === 403) {
        var loginChk = confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
        if (loginChk) {
          window.location.href = "login.html";
        }
      } else {
        var cartChk = confirm(
          "상품을 장바구니에 담았습니다. 장바구니로 이동하시겠습니까?"
        );
        if (cartChk) {
          window.location.href = "cart.html";
        }
      }
    })
    .catch((error) => console.error("Error:", error));
}

function addOrder() {
  var orderUrl = commonUrl + "orders";
  var quantity = document.getElementById("input-quantity").value;

  var data = [
    {
      productId: productId,
      quantity: quantity,
    },
  ];

  var orderChk = confirm(
    "주문을 진행하시겠습니다? 예를 누르면 주문처리 됩니다."
  );

  if (orderChk) {
    fetch(orderUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        return response.json();
      })
      .then((jsonData) => {
        if (jsonData.status === 403) {
          var loginChk = confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
          if (loginChk) {
            window.location.href = "login.html";
          }
        } else {
          alert("주문 처리되었습니다.");
        }
      });
  } else {
    alert("주문을 취소하였습니다.");
  }
}
