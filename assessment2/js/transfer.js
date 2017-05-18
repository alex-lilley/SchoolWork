//atm interactions
$(document).ready(function(){
    console.log("TRANSFER.HTML");
    //all interaction codes for machine.html goes in here
    payments = document.getElementById("payments");
    transfer = document.getElementById("transfer");
    statistics = document.getElementById("statistics");
    logout = document.getElementById("logout");
    numBox = document.getElementById("numberBox");
    makePayment = document.getElementById("enter");
    amount = document.getElementById("amount");
    var type = "";
    payments.addEventListener("click", function(){
        numBox.style.display = "inline-block";
        type = "payment";
    })
    transfer.addEventListener("click", function(){
        numBox.style.display = "inline-block";
        type = "transfer";
    })
    makePayment.addEventListener("click",function(){
        $.ajax({
            url:"/payment",
            type:"post",
            data:{
                amount:amount.value,
                type:type
            },
            success:function(resp){
                if(resp.status == "success"){
                    console.log("amount transferred");
                } else {
                    alert("unable to transfer")
                }
            }
        })
    })
    logout.addEventListener("click",function(){
        $.ajax({
            url:"/logout",
            type:"post",
            success:function(resp){
                if(resp.status == "success"){
                    console.log("logged out");
                    location.href ="/";
                } else {
                    alert("error logging out")
                }
            }
        })
    })
    statistics.addEventListener("click",function(){
        location.href = "/statistics"
    })
});