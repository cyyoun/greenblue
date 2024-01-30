const reviewUrl = "http://localhost:8080/reviews";

let sort = "new";
let page = 0;

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

    const container = document.querySelector(".review-container");
    container.innerHTML = "";
    data.forEach((item) => {
      var review = document.createElement("div");
      review.className = "review";

      var basicText = document.createElement("div");
      basicText.className = "review-basic-text";

      var user = document.createElement("div");
      user.className = "user";
      user.textContent = item.username;

      var regDate = document.createElement("div");
      regDate.className = "reg-date";
      const originalDate = new Date(item.regDate);
      const formatter = new Intl.DateTimeFormat("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: true, // 24시간 형식
        timeZone: "Asia/Seoul", // 한국 시간대
      });

      const formattedDate = formatter.format(originalDate);
      regDate.textContent = formattedDate;

      var title = document.createElement("div");
      title.className = "title";
      title.textContent = item.title;

      var score = document.createElement("div");
      var starsText = "";
      for (var i = 0; i < item.score; i++) {
        starsText += "⭐";
      }
      score.className = "rating";
      score.textContent = starsText;

      var content = document.createElement("div");
      content.className = "content";
      content.textContent = item.content;

      var images = document.createElement("div");
      images.className = "review-image";
      images.innerHTML = "";

      item.reviewImgDtoList.forEach((reviewImage) => {
        var img = document.createElement("img");
        var imgPath = `../img/review/${reviewImage.filename}`;
        img.src = imgPath;
        img.onclick = function () {
          createModal(imgPath);
        };
        images.appendChild(img);
      });

      basicText.appendChild(user);
      basicText.appendChild(regDate);
      review.appendChild(basicText);
      review.appendChild(title);
      review.appendChild(score);
      review.appendChild(content);
      review.appendChild(images);
      container.appendChild(review);
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
    var currentIndex = reviewImageList.indexOf(img.src);
    var nextIndex = (currentIndex + 1) % reviewImageList.length;
    img.src = reviewImageList[nextIndex];
  }
}

function createPages() {
  var pagesPerView = 5;
  var totalViews = Math.ceil(totalPages / pagesPerView);
  var currentView = Math.ceil((currentPage + 1) / pagesPerView);
  var startPage = (currentView - 1) * pagesPerView + 1;
  var endPage = Math.min(startPage + pagesPerView - 1, totalPages);

  var container = document.getElementById("pagination-container");
  container.innerHTML = "";

  var leftBut = document.createElement("a");
  leftBut.textContent = "«";
  leftBut.addEventListener("click", () => {
    if (currentView > 1) {
      page = startPage - 2;
      getReviews();
    }
  });
  container.appendChild(leftBut);

  for (let i = startPage; i <= endPage; i++) {
    var pageNumber = document.createElement("a");
    pageNumber.textContent = i;
    pageNumber.addEventListener("click", () => {
      page = i - 1;
      getReviews();
    });
    container.appendChild(pageNumber);
  }

  var rightBut = document.createElement("a");
  rightBut.textContent = "»";
  rightBut.addEventListener("click", () => {
    if (currentView < totalViews) {
      page = endPage;
      getReviews();
    }
  });
  container.appendChild(rightBut);
}
