import { postData } from "./common/submitPost.js";

const joinButton = document.getElementById("join");
if (joinButton) {
  joinButton.addEventListener("click", function () {
    ValidateAndSubmit();
  });
}

function ValidateAndSubmit() {
  const validate = validateForm();
  if (validate.isValid) {
    submitData();
  } else {
    console.log(validate.message);
    window.alert(validate.message);
  }
}

function validateForm() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const email = document.getElementById("email").value;

  if (username === "") {
    return { isValid: false, message: "아이디를 입력해주세요" };
  } else if (password === "") {
    return { isValid: false, message: "비밀번호를 입력해주세요" };
  } else if (email === "") {
    return { isValid: false, message: "이메일 주소를 입력해주세요." };
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    return { isValid: false, message: "이메일 형식이 올바르지 않습니다." };
  }

  return { isValid: true, message: "" };
}

function submitData() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const email = document.getElementById("email").value;

  const url = "/join";
  const data = {
    username: username,
    password: password,
    email: email,
  };

  function successCallback(data) {
    window.location.href = "/html/home.html";
  }

  function errorCallback(errorMessage) {
    window.alert(errorMessage);
  }

  postData(url, data, successCallback, errorCallback);
}
