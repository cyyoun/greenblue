const host = "http://localhost:8080";

const loginButton = document.getElementById("login");
if (loginButton) {
  loginButton.addEventListener("click", function () {
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

  if (username === "") {
    return { isValid: false, message: "아이디를 입력해주세요" };
  } else if (password === "") {
    return { isValid: false, message: "비밀번호를 입력해주세요" };
  }
  return { isValid: true, message: "" };
}

function submitData() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const data = {
    username: username,
    password: password,
  };

  fetch(host + "/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      const authHeader = response.headers.get("Authorization");

      if (authHeader && authHeader.startsWith("Bearer ")) {
        const token = authHeader.slice(7);
        setTokenCookie("jwt", token);
        window.location.href = "/html/home.html";
      } else {
        throw new Error("로그인 실패");
      }
    })
    .catch((error) => {
      window.alert(error);
    });
}

function setTokenCookie(name, token) {
  var date = new Date();
  date.setTime(date.getTime() + 60 * 60 * 1000);
  document.cookie =
    name + "=" + token + ";expires=" + date.toUTCString() + ";path=/";
}
