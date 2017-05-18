//DON'T MODIFY ANY EXISTING CODES
const express = require("express");
const path = require("path");
const NEWPORT = process.env.PORT || 12345;
const pg = require("pg");
const session = require("express-session");
const bodyParser = require("body-parser");

var CLF = path.resolve(__dirname,"cli");

var app = express();

var dbURL = process.env.DATABASE_URL || "postgres://postgres:fireice27090362@localhost:5432/online_banking";

const sv = require("http").createServer(app);
//create a socket server with the new server
var io = require("socket.io")(sv);
app.use(bodyParser.urlencoded({
    extended:true
}));
app.use("/sfxdde", express.static("js"));
app.use("/scripts", express.static("build"));
app.use(session({
    //session settings
    secret:"you would never guess this amirite",
    resave:true,
    saveUninitialized: true
}));
//root folder
app.get("/", function(req, resp){
    resp.sendFile(CLF+"/main.html");
});
app.get("/statistics", function(req, resp){
    if (req.session.user){
    resp.sendFile(CLF+"/statistics.html");
    } else {
        resp.sendFile(CLF+"/main.html");
    }
});
app.get("/transfer", function(req,resp){
    if (req.session.user){
    resp.sendFile(CLF+"/transfer.html");
    } else {
        resp.sendFile(CLF+"/main.html");
    }
})
app.post("/regUser", function(req,resp){
    var password = req.body.password;
    var name = req.body.name;
    var secret = req.body.secret;
    var question = req.body.question;
    
    pg.connect(dbURL, function(err, client, done){
       if(err){
            console.log(err);
            resp.end("FAIL");
        }
        client.query("insert into ob_accounts (password, name, secret, question) VALUES ($1,$2,$3,$4)",[password,name,secret,question],function(err, result){
            done();
            if(err){
                console.log(err);
                resp.end("FAIL");
            }
            if(result.rows.length >0){
                resp.end("FAIL");

            }
            else {
                var obj = {
                    status:"success"
                }
                resp.send(obj);
            }
        })
    })
})
app.post("/payment", function(req,resp){
    var amount = req.body.amount;
    var type = req.body.type;
    var id = req.session.user.id;
    pg.connect(dbURL, function(err, client, done){
       if(err){
            console.log(err);
            resp.end("FAIL");
        }
        client.query("insert into ob_transfers (amount, ob_accounts_id, transfer_type) VALUES ($1,$2,$3)",[amount,id,type],function(err, result){
            done();
            if(err){
                console.log(err);
                resp.end("FAIL");
            }
            if(result.rows.length >0){
                resp.end("FAIL");

            }
            else {
                var obj = {
                    status:"success"
                }
                resp.send(obj);
            }
        })
    })
})
app.post("/logout", function(req,resp){
    req.session.destroy();
    var obj = {
        status:"success"
    }
    resp.send(obj);
})
app.post("/login", function(req,resp){
    var password = req.body.password;
    var name = req.body.name;
    var secret = req.body.secret;
    
    pg.connect(dbURL, function(err, client, done){
       if(err){
            console.log(err);
            resp.end("FAIL");
        }
        client.query("select * from ob_accounts where password=$1 and name=$2 and secret=$3",[password,name,secret],function(err, result){
            if(err){
                console.log(err);
                resp.end("FAIL");
            }
            if(result.rows.length >0){
                done();
                req.session.user = result.rows[0];
                var obj = {
                    status:"success"
                }
                resp.send(obj);
            }
            else {
                client.query("select * from ob_accounts where password=$1 and name=$2",[password,name],function(err, result){
                    if(err){
                        console.log(err);
                        resp.end("FAIL");
                    }
                    if(result.rows.length >0){
                        done();
                        var obj = {
                            status:"partly",
                            questionValue:result.rows[0].question
                        }
                        resp.send(obj);
                    } else {
                        resp.end("FAIL");
                    }
                })
            }
        })
    })
})
app.post("/loadPayments", function(req,resp){
    var id = req.session.user.id;

    pg.connect(dbURL, function(err, client, done){
       if(err){
            console.log(err);
            resp.end("FAIL");
        }
        client.query("select * from ob_transfers where ob_accounts_id=$1",[id],function(err, result){
            if(err){
                console.log(err);
                resp.end("FAIL");
            }
            if(result.rows.length >0){
                done();
                var obj = {
                    status:"success",
                    transactions:result
                }
                resp.send(obj);
            } else {
                var obj = {
                    status:"noTransactions"
                }
            }

        })
    })
})
//listen to the port
sv.listen(NEWPORT, function(err){
    if(err){
        console.log(err);
        return false;
    }
    
    console.log(NEWPORT+" is running");
});