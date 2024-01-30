var commonUrl = "http://localhost:8080/";

var selectedRating = 0;
var title;
var content;

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

function addReview() {
  var reviewUrl = commonUrl + "reviews";
  title = document.getElementById("title").value;
  content = document.getElementById("content").value;
  var orderProductId = localStorage.getItem("orderProductId");
  var productId = localStorage.getItem("productId");
  var images = document.getElementById("img").files;
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
        alert("리뷰가 작성되었습니다.");
        window.location.href = "../html/product.html?id=" + productId;
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
