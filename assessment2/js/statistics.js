$(document).ready(function(){
    transactionList = document.getElementById("transactionList");
    transactionSum = document.getElementById("transactionSum");
    back = document.getElementById("back");
    $.ajax({
        url:"/loadPayments",
        type:"post",
        success:function(resp){
            if(resp.status == "success"){
                console.log(resp);
                var sum = 0;
                for(i=0;i<resp.transactions.rows.length;i++){
                    var transaction = document.createElement("div");
                    transaction.innerHTML = resp.transactions.rows[i].amount + " => " + resp.transactions.rows[i].transfer_type;
                    transactionList.appendChild(transaction);
                    sum += resp.transactions.rows[i].amount;
                }
                transactionSum.innerHTML = "total transferred: " +sum;
            } else if(resp.status =="noTransactions"){
                alert("no current transactions");
            } else {
                alert("error loading transactions");
            }
        }
    })
    back.addEventListener("click",function(){
        location.href = "/transfer";
    })
})