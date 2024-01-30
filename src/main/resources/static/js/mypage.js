const commonUrl = "http://localhost:8080/";

window.onload = () => {
  memberStatusSetting();
  getOrderList();
};

function memberStatusSetting() {
  var memberUrl = commonUrl + "member";
  var pointUrl = commonUrl + "point";

  fetch(memberUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      var username = jsonData.username;
      var grade = jsonData.grade;
      var infoElement = document.getElementById("member-info");
      infoElement.innerText = `${username} 고객님은 현재 ${grade} 등급입니다.`;
    });

  fetch(pointUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      var point = `현재 포인트 : ${jsonData.toLocaleString()}원`;
      var pointElement = document.getElementById("member-point");
      pointElement.innerText = point;
    });
}

function getOrderList() {
  var orderUrl = commonUrl + "orders";

  fetch(orderUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createOrderlist(jsonData);
    });
}

function createOrderlist(data) {
  var rowCnt = 5;
  var trElement;
  var container = document.getElementById("order-container");
  container.innerHTML = "";
  data.forEach((item, index) => {
    if (index % rowCnt === 0) {
      trElement = document.createElement("tr");
    }
    var tdElement = document.createElement("td");
    tdElement.className = "order-item";
    tdElement.id = "order-product-" + item.id;

    var aElement = document.createElement("a");
    aElement.href = "../html/product.html?id=" + item.productId;

    var imgElement = document.createElement("img");
    imgElement.className = "item-img";
    imgElement.src = "../img/product/" + item.mainImgFilename;

    var name = document.createElement("div");
    name.innerText = `상품명 : ${item.productName}`;

    var price = document.createElement("div");
    price.innerText = `금액 : ${item.productPrice.toLocaleString()}`;

    var quantity = document.createElement("div");
    quantity.innerText = `수량 : ${item.quantity}`;

    var purchaseBut = document.createElement("button");
    purchaseBut.innerText = returnPurchaseStatus(item.purchaseStatus);
    purchaseBut.onclick = function () {
      purchaseConfirm(item.id);
    };
    if (item.purchaseStatus === "PURCHASE_UNCONFIRM") {
      purchaseBut.className = "active-button";
      purchaseBut.disabled = false;
    } else {
      purchaseBut.className = "default-button";
      purchaseBut.disabled = true;
    }

    var reviewBut = document.createElement("button");
    reviewBut.innerText = returnReviewStatus(item.reviewStatus);
    reviewBut.onclick = function () {
      localStorage.setItem("orderProductId", item.id);
      localStorage.setItem("productId", item.productId);
      window.location.href = "../html/createReview.html";
    };
    if (item.reviewStatus === "UNWRITTEN") {
      reviewBut.className = "active-button";
      reviewBut.disabled = false;
    } else {
      reviewBut.className = "default-button";
      reviewBut.disabled = true;
    }

    aElement.appendChild(imgElement);
    tdElement.appendChild(aElement);
    tdElement.appendChild(name);
    tdElement.appendChild(price);
    tdElement.appendChild(quantity);
    tdElement.appendChild(purchaseBut);
    tdElement.appendChild(reviewBut);
    trElement.appendChild(tdElement);
    if (index % rowCnt === 0 || data.length - 1 === index) {
      container.appendChild(trElement);
    }
  });
}

function returnReviewStatus(reviewStatus) {
  switch (reviewStatus) {
    case "UNWRITTEN":
      return "리뷰 작성";
    case "WRITTEN":
      return "리뷰 작성 완료";
    case "ACCRUAL":
      return "리뷰 작성 완료";
    case "NON_ACCRUAL":
      return "리뷰 작성 기간 만료";
    case "DELETE":
      return "리뷰 삭제";
  }
}

function returnPurchaseStatus(purchaseStatus) {
  switch (purchaseStatus) {
    case "PURCHASE_UNCONFIRM":
      return "구매 확정";
    case "PURCHASE_CONFIRM":
      return "구매 확정 완료";
    case "ACCRUAL":
      return "구매 확정 완료";
    case "NON_ACCRUAL":
      return "자동 구매 확정";
    case "PURCHASE_CANCEL":
      return "구매 취소";
  }
}

function purchaseConfirm(orderProductId) {
  var confirmChk = confirm(
    "구매확정하시겠습니까? 구매확정 하는 경우 환불이 불가합니다."
  );
  if (confirmChk) {
    var confirmUrl = commonUrl + "orders/confirms";
    fetch(confirmUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify([orderProductId]),
    }).then((response) => {
      if (response.status === 200) {
        memberStatusSetting();
        getOrderList();
      }
    });
  }
}
