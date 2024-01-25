const reviewUrl = "http://localhost:8080/reviews";

let productId = "0";
let sort = "new";
let page = "0";

let totalPages = 0;
let currentPage = 0;

function conditionSearch() {
  sort = document.querySelector('input[name="score"]:checked').id;
  getReviews();
}

function getReviews() {
  const currentUrl = window.location.href;
  const productIdMatch = currentUrl.match(/id=(\d{1,})/);
  productId = productIdMatch ? parseInt(productIdMatch[1], 10) : null;

  const url = `${reviewUrl}?product-id=${productId}&sort=${sort}&page=${page}`;

  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createReviews(jsonData.content);
      totalPages = jsonData.totalPages;
      currentPage = jsonData.number;
      createPages();
    });
}

function createReviews(data) {
  const avgScore = document.getElementById("avg-score");
  if (data.length === 0) {
    avgScore.textContent = "🙂 작성 후기가 없습니다.";
    const reviews = document.querySelector(".reviews");
    reviews.style.display = "none";
  } else {
    let avg = 0;
    data.forEach((item) => {
      avg += item.score;
    });
    avg = avg / data.length;
    avgScore.textContent = `🙂 전체 평점 ${avg} 점 (최대 5점)`;

    data.forEach((item) => {
      const container = document.getElementById("review-container");
      const header = document.getElementById("review-header");
      const user = document.getElementById("user");
      user.textContent = item.username;

      const regDate = document.getElementById("reg-date");
      regDate.textContent = item.regDate;

      const title = document.querySelector(".title");
      title.textContent = item.title;

      const score = document.querySelector(".rating");
      let starsText = "";
      for (var i = 0; i < item.score; i++) {
        starsText += "⭐";
      }
      score.textContent = starsText;

      const content = document.querySelector(".content");
      content.textContent = item.content;

      const imgSelector = document.querySelector(".review-image");
      imgSelector.innerHTML = "";

      item.reviewImgDtoList.forEach((reviewImage) => {
        const img = document.createElement("img");
        const imgPath = `../img/review/${reviewImage.filename}`;
        img.src = imgPath;
        img.onclick = function () {
          createModal(imgPath);
        };
        imgSelector.appendChild(img);
      });

      header.appendChild(user);
      header.appendChild(regDate);
      container.appendChild(header);
      container.appendChild(title);
      container.appendChild(score);
      container.appendChild(content);
      container.appendChild(imgSelector);
    });
  }
}

function createModal(imagePath) {
  var existingModal = document.querySelector(".modal");
  if (existingModal) {
    document.body.removeChild(existingModal);
  }

  var modal = document.createElement("div");
  modal.classList.add("modal");

  var closeButton = document.createElement("span");
  closeButton.classList.add("close-modal");
  closeButton.innerHTML = "&times;";
  closeButton.onclick = function () {
    document.body.removeChild(modal);
  };

  var img = document.createElement("img");
  img.classList.add("modal-img");
  img.src = imagePath;

  var nextButton = document.createElement("button");
  nextButton.classList.add("next-Button");
  nextButton.innerHTML = "Next";
  nextButton.onclick = function () {
    showNextImage(img);
  };

  modal.appendChild(closeButton);
  modal.appendChild(img);
  modal.appendChild(nextButton);
  document.body.appendChild(modal);
}

function showNextImage(img) {
  var imgContainer = document.querySelector(".review-image");
  var imgElements = imgContainer.querySelectorAll("img");
  var reviewImageList = Array.from(imgElements).map((img) => img.src);

  if (reviewImageList.length > 0) {
    const currentIndex = reviewImageList.indexOf(img.src);
    const nextIndex = (currentIndex + 1) % reviewImageList.length;
    img.src = reviewImageList[nextIndex];
  }
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
      getReviews();
    }
  });
  container.appendChild(leftBut);

  for (let i = startPage; i <= endPage; i++) {
    const pageNumber = document.createElement("a");
    pageNumber.textContent = i;
    pageNumber.addEventListener("click", () => {
      page = i - 1;
      getReviews();
    });
    container.appendChild(pageNumber);
  }

  const rightBut = document.createElement("a");
  rightBut.textContent = "»";
  rightBut.addEventListener("click", () => {
    if (currentView < totalViews) {
      page = endPage;
      getReviews();
    }
  });
  container.appendChild(rightBut);
}
