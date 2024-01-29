const commonUrl = "http://localhost:8080/";

window.onload = () => {
  getCartList();
};

function getCartList() {
  var cartUrl = commonUrl + "carts";
  fetch(cartUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createCartList(jsonData);
    });
}
function createCartList(data) {
  var rowCnt = 3;
  var trElement;
  var container = document.querySelector(".cart");
  container.innerHTML = "";
  data.forEach((item, index) => {
    if (index % rowCnt === 0) {
      trElement = document.createElement("tr");
    }
    var product = item.productOutputDto;
    var tdElement = document.createElement("td");
    var lableElement = document.createElement("label");
    lableElement.for = "cart-" + item.id;

    var info = document.createElement("div");
    info.className = "product-info";

    var chkbox = document.createElement("div");
    var inputChkbox = document.createElement("input");
    inputChkbox.type = "checkbox";
    inputChkbox.id = "cart-" + item.id;

    var img = document.createElement("img");
    img.src = "../img/product/" + product.mainImgDto.filename;
    img.className = "product-img";

    var name = document.createElement("div");
    name.className = "name";
    name.innerText = product.name;

    var quantity = document.createElement("div");
    quantity.setAttribute("product-id", product.id);
    quantity.className = "quantity";
    quantity.innerText = item.quantity + " 개";

    var price = document.createElement("div");
    price.className = "price";
    price.innerText = product.price;

    chkbox.appendChild(inputChkbox);
    info.appendChild(chkbox);
    info.appendChild(img);
    info.appendChild(name);
    info.appendChild(quantity);
    info.appendChild(price);
    lableElement.appendChild(info);
    tdElement.appendChild(lableElement);
    trElement.appendChild(tdElement);
    if (index % rowCnt === 0 || index === data.length - 1) {
      container.appendChild(trElement);
    }
  });
}

function addOrder() {
  var orderUrl = commonUrl + "orders";
  var chkboxList = document.querySelectorAll('input[type="checkbox"]');
  var chkValues = [];

  chkboxList.forEach((item) => {
    if (item.checked) {
      var productInfo = item.closest(".product-info");
      var quantityElement = productInfo.querySelector(".quantity");
      var quantity = quantityElement.textContent.split(" 개")[0];
      var productId = quantityElement.getAttribute("product-id");

      var data = {
        productId: productId,
        quantity: quantity,
      };
      chkValues.push(data);
    }
  });

  var orderChk = confirm(
    "주문을 진행하시겠습니다? 예를 누르면 주문처리 됩니다."
  );

  if (orderChk) {
    fetch(orderUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(chkValues),
    })
      .then((response) => {
        console.log("response", response);
        return response.json();
      })
      .then((jsonData) => {
        console.log(jsonData);
        if (jsonData.status === 403) {
          var loginChk = confirm("로그인이 필요합니다. 로그인 하시겠습니까?");
          if (loginChk) {
            window.location.href = "login.html";
          }
        } else {
          alert("주문 처리되었습니다.");
          getCartList();
        }
      });
  } else {
    alert("주문을 취소하였습니다.");
  }
}

function deleteCart() {
  var cartUrl = commonUrl + "carts/delete";
  var chkboxList = document.querySelectorAll('input[type="checkbox"]');
  var cartIdList = [];

  chkboxList.forEach((item) => {
    if (item.checked) {
      var cartId = item.id.split("-")[1];
      cartIdList.push(cartId);
    }
  });

  fetch(cartUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(cartIdList),
  }).then((response) => {
    if (response.status === 200) {
      alert("상품을 장바구니에서 삭제하였습니다.");
      getCartList();
    }
  });
}
