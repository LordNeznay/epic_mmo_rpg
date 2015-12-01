define([
    'backbone',
    'utils/headers',
    'sync/playerSync'
], function(
    Backbone,
    headers,
    playerSync
){

    var Model = Backbone.Model.extend({
        defaults: {
            name: "",
            isLogin: false,
            score: 0,
            ws: null,
            isOpenedSocket: false,
            isInGame: false,
            isWait: true,
            isGameComplite: false
        },
        
        initialize: function(){
            this.on("joinGame", this.joinGame, this);
        },
        
        socket_open: function(){
            if(!this.isLogin) return;
            var that = this;

            this.ws = new WebSocket('ws://' + document.location.host + '/gameplay');
            this.ws.onopen = function (event) {
                console.log("Socket was opened\n");
                that.isOpenedSocket = true;
                that.trigger("joinGame");
            }
            this.ws.onmessage = function (event) {
                console.log(event.data);
                var allData = JSON.parse(event.data);
                //console.log(allData);
                allData.forEach(function(data){
                    switch(data.t){
                        case headers.waitStart:{
                            that.isGameComplite = false;
                            that.isWait = true;
                            that.trigger("isWait:change");
                        }; break;
                        case headers.userWasJoined:{
                            that.isInGame = true;
                            that.isWait = false;
                            that.trigger("isWait:change");
                        }; break;
                        case headers.gameResult:{
                            that.isGameComplite = true;
                            that.trigger("gameResult", data.b.gameResult, data.b.playerCommand);
                        }; break; 
                        case headers.viewArea:{
                            that.trigger("loadMap", data.b);
                        }; break;
                        case headers.playerPosition:{
                            that.trigger("playerPosition", data.b);
                        }; break;
                        case headers.entitiesInViewArea:{
                            that.trigger("loadEntities", data.b);
                        }; break;
                        case headers.availableActions:{
                            that.trigger("availableActions", data.b);
                        }; break;       
                        case headers.flagStatus:{
                            that.trigger("flagStatus", data.b);
                        }; break; 
                        case headers.entityStatus:{
                            that.trigger("entityStatus", data.b);
                        }; break;     
                        case headers.abilityStatus:{
                            that.trigger("abilityStatus", data.b);
                        }; break;   
                        default: break;
                    }
                });
            }
            this.ws.onerror = function(){
                console.log("Socket was not opened\n");
                that.isOpenedSocket = false;
                Backbone.history.navigate('', true);
            },
            this.ws.onclose = function (event) {
                console.log("Socket was closed\n");
                that.isOpenedSocket = false;
                Backbone.history.navigate('', true);
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
        
        ability: function(name){
            var message = '{"command": "action", "action" : "useAbility", "abilityName" : "' + name + '"}';
            this.sendMessage(message);
        },
                
        startCapture: function(){
            var message = '{"command": "action", "action" : "flagCapture"}';
            this.sendMessage(message);
            //alert(message);
        },
        
        getCoord: function(){
            var message = '{"command": "getcoord"}';
            ws.send(message);
        },
        
        
        
        
        
        sync: playerSync,
        
        
        status: function(callback){
            this.sync('read', this, {callback: callback});
        },
        login: function(param, callback){
            this.sync('update', this, {callback: callback, param: param});

        },
        unlogin: function(callback){
            this.sync('delete', this, {callback: callback});  
        },
        registration: function(param, callback){
            this.sync('singin', this, {callback: callback, param: param});
        }

    });

    var user = new Model();
    user.fetch();

    return user;
});