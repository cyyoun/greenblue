const categoryUrl = "http://localhost:8080/categories";
const productUrl = "http://localhost:8080/products";

let categoryName = "";
let subCategoryName = "";
let categoryId = "0";

let page = "0";
let sort = "new";
let startPrice = "0";
let endPrice = "9999999";
let soldOut = "y";

let totalPages = 0;
let currentPage = 0;

window.onload = () => {
  toggleButtons();
  getCategories();
  previousPageLoad();
};

function previousPageLoad() {
  var previousPage = localStorage.getItem("previousPage");
  if (previousPage) {
  }

  categoryId = getParameterByName("category-id");
  page = getParameterByName("page");
  sort = getParameterByName("sort");
  startPrice = getParameterByName("start-price");
  endPrice = getParameterByName("end-price");
  soldOut = getParameterByName("sold-out");

  getProducts();
  createCategoryTree();

  function getParameterByName(name) {
    name = name.replace(/[\[\]]/g, "\\$&");
    const regex = new RegExp(`[?&]${name}(=([^&#]*)|&|#|$)`);
    const results = regex.exec(previousPage);
    return results ? decodeURIComponent(results[2].replace(/\+/g, " ")) : null;
  }
  sessionStorage.clear(previousPage);
}

function logout() {
  document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  alert("로그아웃 되었습니다.");
  toggleButtons();
}

function toggleButtons() {
  var loginButton = document.getElementById("login-button");
  var logoutButton = document.getElementById("logout-button");
  var joinButton = document.getElementById("join-button");
  var cartButton = document.getElementById("cart-button");
  var mypageButton = document.getElementById("mypage-button");

  if (getCookie("jwt")) {
    loginButton.style.display = "none";
    joinButton.style.display = "none";
    logoutButton.style.display = "block";
    cartButton.style.display = "block";
    mypageButton.style.display = "block";
  } else {
    loginButton.style.display = "block";
    joinButton.style.display = "block";
    logoutButton.style.display = "none";
    cartButton.style.display = "none";
    mypageButton.style.display = "none";
  }
}

function getCookie(name) {
  var cookieArr = document.cookie.split(";");

  for (var i = 0; i < cookieArr.length; i++) {
    var cookiePair = cookieArr[i].split("=");
    if (name === cookiePair[0].trim()) {
      return decodeURIComponent(cookiePair[1]);
    }
  }
  return null;
}

function getCategories() {
  fetch(categoryUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createCategories(jsonData);
    });
}

function createCategories(data) {
  const container = document.getElementById("menu-bar-container");
  data.forEach((item) => {
    const button = document.createElement("button");
    button.textContent = item.name;
    button.className = "menu-button";
    button.id = "category-" + item.id;
    button.addEventListener("click", () => {
      categoryName = item.name;
      localStorage.setItem("category", "category-" + item.id);
      getSubCategories(item.id);
    });
    container.appendChild(button);
  });
  clickButtonBySession(localStorage.getItem("category"));
}

function getSubCategories(id) {
  const url = categoryUrl + "/" + id;
  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createSubCategories(jsonData);
    });
}

function createSubCategories(data) {
  const container = document.getElementById("submenu-bar-container");
  container.innerHTML = "";

  data.forEach((item) => {
    const listButton = document.createElement("li");
    const link = document.createElement("a");
    link.textContent = item.name;
    link.className = "submenu-item";
    link.id = "subCategory-" + item.id;
    link.addEventListener("click", () => {
      categoryId = item.id;
      subCategoryName = item.name;
      localStorage.setItem("subCategory", "subCategory-" + item.id);
      getProducts();
      createCategoryTree();
    });
    listButton.appendChild(link);
    container.appendChild(listButton);
  });
  clickButtonBySession(localStorage.getItem("subCategory"));
}

function getProducts() {
  let url = `${productUrl}/list?category-id=${categoryId}&page=${page}&sort=${sort}&start-price=${startPrice}&end-price=${endPrice}&sold-out=${soldOut}`;
  console.log("url : ", url);

  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createProducts(jsonData.content);
      totalPages = jsonData.totalPages;
      currentPage = jsonData.number;
      createPages();
      localStorage.setItem("previousPage", url);
    });
}

function createCategoryTree() {
  const categoryTree = document.getElementById("category-tree");
  categoryTree.innerText = categoryName + " > " + subCategoryName;
}

function createProducts(data) {
  const container = document.getElementById("product-container");
  container.innerHTML = "";
  const trCnt = 3;
  let trElement;
  data.forEach((item, index) => {
    if (index % trCnt === 0) {
      trElement = document.createElement("tr");
      container.appendChild(trElement);
    }
    const tdElement = document.createElement("td");

    const aElement = document.createElement("a");
    aElement.href = `../html/product.html?id=${item.id}`;
    aElement.className = "item-text";

    const imgElement = document.createElement("img");
    imgElement.src = "../img/product/" + item.mainImgDto.filename;
    imgElement.className = "item-img";

    const productName = document.createElement("div");
    productName.textContent = item.name;

    const productPrice = document.createElement("div");
    productPrice.textContent = item.price;

    aElement.appendChild(imgElement);
    aElement.appendChild(productName);
    aElement.appendChild(productPrice);
    tdElement.appendChild(aElement);
    trElement.appendChild(tdElement);
  });
}

function createPages() {
  const pagesPerView = 5;
  const totalViews = Math.ceil(totalPages / pagesPerView);
  const currentView = Math.ceil((currentPage + 1) / pagesPerView);
  const startPage = (currentView - 1) * pagesPerView + 1;
  const endPage = Math.min(startPage + pagesPerView - 1, totalPages);

  const container = document.getElementById("pagination-container");
  container.innerHTML = "";
  const leftBut = document.createElement("a");
  leftBut.textContent = "«";
  leftBut.addEventListener("click", () => {
    if (currentView > 1) {
      page = startPage - 2;
      getProducts();
    }
  });
  container.appendChild(leftBut);

  for (let i = startPage; i <= endPage; i++) {
    const pageNumber = document.createElement("a");
    pageNumber.textContent = i;
    pageNumber.addEventListener("click", () => {
      page = i - 1;
      getProducts(categoryId);
    });
    container.appendChild(pageNumber);
  }

  const rightBut = document.createElement("a");
  rightBut.textContent = "»";
  rightBut.addEventListener("click", () => {
    if (currentView < totalViews) {
      page = endPage;
      getProducts();
    }
  });
  container.appendChild(rightBut);
}

function conditionSearch() {
  sort = document.querySelector('input[name="order"]:checked').id;
  startPrice = document.getElementById("startPrice").value;
  endPrice = document.getElementById("endPrice").value;
  soldOut = document.getElementById("soldout-chk").checked === true ? "n" : "y";
  getProducts();
}

function resetCondition() {
  document.getElementById("startPrice").value = "";
  document.getElementById("endPrice").value = "";
  document.getElementById("new").checked = true;
  document.getElementById("soldout-chk").checked = false;
  conditionSearch();
}

function keywordSearch() {
  const word = document.getElementById("keyword").value;
  const url = `${productUrl}?word=${word}`;
  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      const categoryTree = document.getElementById("category-tree");
      if (jsonData.length > 0) {
        categoryTree.innerText = word + " 검색 결과입니다.";
      } else {
        categoryTree.innerText = word + " 검색 결과가 없습니다.";
      }
      const condition = document.querySelector(".condition");
      condition.style.display = "none";
      createProducts(jsonData);
    });
}

function clickButtonBySession(session) {
  if (session) {
    var getButton = document.getElementById(session);
    getButton.click();
  }
}
