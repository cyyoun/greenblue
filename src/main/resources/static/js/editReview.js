var commonUrl = "http://localhost:8080/";

window.onload = () => {
  getReview();
};

var orderProductId = localStorage.getItem("orderProductId");
var reviewId = localStorage.getItem("reviewId");

var deleteImgList = [];
var selectedRating = 0;
var title;
var content;

function getReview() {
  var reviewUrl = commonUrl + "reviews/" + reviewId;
  fetch(reviewUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      setReview(jsonData);
    });
}

function setReview(data) {
  var titleElement = document.getElementById("title");
  titleElement.value = data.title;

  var contentElement = document.getElementById("content");
  contentElement.textContent = data.content;

  setRating(data.score);

  var container = document.querySelector(".img-container");
  container.innerHTML = "";

  if (data.reviewImgDtoList.length > 0) {
    data.reviewImgDtoList.forEach((item) => {
      var imgElement = document.createElement("img");
      imgElement.src = "../img/review/" + item.filename;
      imgElement.id = "img-" + item.id;

      var imgCloseMemo = document.createElement("div");
      imgCloseMemo.className = "close-memo";
      imgCloseMemo.textContent = "이미지를 클릭하면 삭제됩니다.";

      imgElement.onclick = () => {
        deleteImgList.push(item.id);
        imgElement.style.display = "none";
      };
      container.appendChild(imgElement);
      container.appendChild(imgCloseMemo);
    });
  }
}

function setRating(value) {
  selectedRating = value;
  var stars = document.querySelectorAll(".rating-stars .star");
  stars.forEach((star, index) => {
    star.classList.remove("filled");
    if (index < value) {
      star.classList.add("filled");
    }
  });
}

function editReview() {
  var reviewUrl = commonUrl + "reviews/" + reviewId;

  title = document.getElementById("title").value;
  content = document.getElementById("content").value;
  var orderProductId = localStorage.getItem("orderProductId");
  var productId = localStorage.getItem("productId");
  var images = document.getElementById("img").files;

  console.log("title ", title);
  console.log("content ", content);
  console.log("orderProductId ", orderProductId);
  console.log("productId ", productId);
  console.log("images ", images);
  console.log("deleteImgList", deleteImgList);
  console.log("score", selectedRating);

  chkReview();

  const reviewData = {
    score: selectedRating,
    title: title,
    content: content,
    orderProductId: orderProductId,
  };
  const formData = new FormData();
  formData.append(
    "review",
    new Blob([JSON.stringify(reviewData)], { type: "application/json" })
  );
  formData.append(
    "deleteImgList",
    new Blob([JSON.stringify(deleteImgList)], { type: "application/json" })
  );
  if (images.length > 0) {
    for (let i = 0; i < images.length; i++) {
      formData.append("multipartFiles", images[i]);
    }
  }
  fetch(reviewUrl, {
    method: "POST",
    body: formData,
  })
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      if (jsonData.code == 400) {
        alert(jsonData.message);
      } else {
        alert("리뷰가 수정되었습니다.");
        window.location.href = "reviews.html";
      }
    })
    .catch((error) => alert("Error:", error));
}

function chkReview() {
  if (selectedRating === 0) {
    alert("별점을 선택해주세요.");
  } else if (title === null || title === "") {
    alert("제목을 입력해주세요.");
  } else if (content === null || content === "") {
    alert("내용을 입력해주세요.");
  }
}
