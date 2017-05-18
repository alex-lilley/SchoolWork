//atm interactions
$(document).ready(function(){
    console.log("MAIN.HTML");
    //all interaction codes for main.html goes in here
    var showLogin = document.getElementById("showLogin");
    var questionText = document.getElementById("questionText");
    var password = document.getElementById("password");
    var secret = document.getElementById("secret");
    var name = document.getElementById("name");
    var question = document.getElementById("question");
    var register = document.getElementById("registerBut");
    var login = document.getElementById("loginBut");
    var regEx = /^[a-zA-Z0-9]{5,10}$/;
    password.addEventListener("keyup", function() {
        if(regEx.test(password.value) == true){
            password.style.color = "#0F0";
        }
        else {
            password.style.color = "#F00";
        }
    })
    secret.addEventListener("keyup", function() {
        if(regEx.test(secret.value) == true){
            secret.style.color = "#0F0";
            if (regEx.test(password.value) == true){
                document.getElementById("registerBut").addEventListener("click", function(){
                    $.ajax({
                        url:"/regUser",
                        type:"post",
                        data:{
                            password:password.value,
                            secret:secret.value,
                            name:name.value,
                            question:question.value
                        },
                        success:function(resp){
                            if(resp.status == "success"){
                                console.log("user created");
                            } else {
                                alert("unable to create user")
                            }
                        }
                    })
                })
            }
        }
        else {
            secret.style.color = "#F00";
        }
    })
    showLogin.addEventListener("click", function(){
        questionText.style.display = "inline-block";
        login.style.display = "inline-block";
        question.style.display= "none";
    })
    login.addEventListener("click", function(){
        $.ajax({
            url:"/login",
            type:"post",
            data:{
                password:password.value,
                name:name.value,
                secret:secret.value
            },
            success:function(resp){
                if(resp.status == "success"){
                        console.log("success logged in");
                        location.href = "/transfer";
                    }else if(resp.status=="partly"){
                        questionText.innerHTML = resp.questionValue;
                    } else {
                        alert("unable to login")
                    }
            }
        })
    })
});