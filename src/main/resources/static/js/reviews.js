const commonUrl = "http://localhost:8080/";

window.onload = () => {
  getMyReviews();
};

function getMyReviews() {
  var reviewUrl = commonUrl + "reviews/mine";
  fetch(reviewUrl)
    .then((response) => {
      return response.json();
    })
    .then((jsonData) => {
      createMyReviews(jsonData);
    });
}

function createMyReviews(data) {
  const avgScore = document.getElementById("avg-score");

  if (data.length === 0) {
    avgScore.textContent = "🙂 작성 후기가 없습니다.";
    const reviews = document.querySelector(".reviews");
    reviews.style.display = "none";
  } else {
    const container = document.querySelector(".review-container");
    container.innerHTML = "";

    data.forEach((item) => {
      var review = document.createElement("div");
      review.className = "review";

      var basicText = document.createElement("div");
      basicText.className = "review-basic-text";

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
        images.appendChild(img);
      });

      var buttonsContainer = document.createElement("div");
      buttonsContainer.className = "buttons-container";
      var editButton = document.createElement("button");
      editButton.className = "edit-button";
      editButton.textContent = "수정";
      editButton.onclick = function () {
        localStorage.setItem("orderProductId", item.orderProductId);
        localStorage.setItem("reviewId", item.id);
        window.location.href = "../html/editReview.html";
      };

      var deleteButton = document.createElement("button");
      deleteButton.className = "delete-button";
      deleteButton.textContent = "삭제";
      deleteButton.onclick = function () {
        deleteReview(item.id);
      };
      buttonsContainer.appendChild(editButton);
      buttonsContainer.appendChild(deleteButton);

      basicText.appendChild(regDate);
      review.appendChild(basicText);
      review.appendChild(title);
      review.appendChild(score);
      review.appendChild(content);
      review.appendChild(images);
      review.appendChild(buttonsContainer);
      container.appendChild(review);
    });
  }
}

function deleteReview(reviewId) {
  var reviewUrl = commonUrl + "reviews/" + reviewId;
  console.log(reviewUrl);
  var deleteChk = confirm("리뷰 삭제 시 복원이 안됩니다. 그래도 하시겠습니까?");
  if (deleteChk) {
    fetch(reviewUrl, {
      method: "DELETE",
    }).then((response) => {
      if (response.status === 200) {
        getMyReviews();
        alert("삭제되었습니다.");
      }
    });
  }
}
