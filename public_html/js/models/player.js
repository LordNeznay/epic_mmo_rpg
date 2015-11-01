define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            name: "",
            isLogin: false,
            score: 0,
            ws: null,
            isOpenedSocket: false,
            isInGame: false
        },
        
        initialize: function(){
            this.on("joinGame", this.joinGame, this);
        },
        
        socket_open: function(){
            //if(!this.isLogin) return;
            var that = this;

            this.ws = new WebSocket('ws://' + document.location.host + '/gameplay');
            this.ws.onopen = function (event) {
                console.log("Socket was opened\n");
                that.isOpenedSocket = true;
                that.trigger("joinGame");
            }
            this.ws.onmessage = function (event) {
                var data = JSON.parse(event.data);
                console.log("Query was got:\n");
                console.log(data);
                switch(data.type){
                    case "user_was_joined":{
                        that.isInGame = true;
                    }; break;
                    case "viewArea":{
                        that.trigger("loadMap", data.map);
                    }; break;
                    case "entitiesInViewArea":{
                        that.trigger("loadEntities", data.entities);
                    }; break;
                    case "availableActions":{
                        that.trigger("availableActions", data.availableActions);
                    }; break;       
                    case "flagStatus":{
                        that.trigger("flagStatus", data.flagStatus);
                    }; break; 
                    case "entityStatus":{
                        that.trigger("entityStatus", data.entityStatus);
                    }; break;                     
                    default: break;
                }
            }
            this.ws.onclose = function (event) {
                console.log("Socket was closed\n");
                that.isOpenedSocket = false;
                Backbone.history.navigate('main', true);
            }
        },
        sendMessage: function(message){
            if(this.isOpenedSocket){
                console.log("Query " + message + " was sended\n");
                this.ws.send(message);
            } else {
                console.log("Error: Socket was not opened!\n");
            }
        },
        
        startGame: function(){
            this.socket_open();
        },
        
        joinGame: function(){
            //alert("joinGame");
            var that = this;
            var message = '{"command": "join_game"}';
            that.sendMessage(message);
        },
        
        leaveGame: function(){
            if(this.isInGame){
                var message = '{"command": "leave_game"}';
                this.sendMessage(message);
                this.isInGame = false;
            }
        },
        
        move: function(params){
            var message = '{"command": "action", "action" : "move", "direction" : "';
            message += params;
            message += '"}';
            this.sendMessage(message);
        },
        
        setTarget: function(x, y){
            var message = '{"command": "action", "action" : "setTarget", "x" : ';
            message += x;
            message += ', "y": ';
            message += y;
            message += '}';
            this.sendMessage(message);
        },
        
        ability1: function(){
            var message = '{"command": "action", "action" : "useAbility", "abilityName" : "OrdinaryHit"}';
            this.sendMessage(message);
        },
        
        ability2: function(){
            var message = '{"command": "action", "action" : "useAbility", "abilityName" : "OrdinaryHealing"}';
            this.sendMessage(message);
        },
        
        
        
        status: function(callback){
            var that = this;
            $.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){	
                    if(data.isLogin == 'true'){
                        that.isLogin = true;
                    } else {
                        that.isLogin = false;
                    }
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });        
        },
        login: function(param, callback){
            var that = this;
            $.ajax({
                type: "POST",
                data: param,
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){
                    if(data.errors == 'null'){
                        that.isLogin = true;
                    }
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });
        },
        unlogin: function(callback){
            var that = this;
            $.ajax({
                type: "POST",
                url: "/api/v1/auth/exit",
                success: function(data){
                    that.isLogin = false;
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });    
        },
        registration: function(param, callback){
            var that = this;
            $.ajax({
                type: "POST",
                data: param,
                url: "/api/v1/auth/signup",
                dataType: 'json',
                success: function(data) {
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });
        },
        
        startCapture: function(){
            var message = '{"command": "action", "action" : "flagCapture"}';
            this.sendMessage(message);
            //alert(message);
        },
        
        getCoord: function(){
            var message = '{"command": "getcoord"}';
            ws.send(message);
        }
    });

    return Model;
});