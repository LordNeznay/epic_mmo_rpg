define([
    'backbone',
    'tmpl/game',
    'underscore',
    'views/base',
    'views/surroundings'
], function(
    Backbone,
    tmpl,
    Underscore,
    BaseView,
    Surroundings
){


    var View = BaseView.extend({
        name: 'game',
        template: tmpl,
        gameField: $(".game-map"),
        
        surroundings: new Surroundings(),
        
        events: {
            "click a": "hide",
        },
        initialize: function(){
            View.__super__.initialize.call(this, {content: '.page-game'});
            _.bindAll(this, 'playerMove');
            $(document).bind('keypress', this.playerMove);
        },
        get_canvas: function(){
            var that = this;
            that.surroundings.canvas_background =  document.getElementById("game-map__background");
            that.surroundings.canvas_background_context = that.surroundings.canvas_background.getContext('2d');
            that.surroundings.canvas_background.width  = 960;     
            that.surroundings.canvas_background.height = 576;
            
            that.surroundings.canvas_middleground =  document.getElementById("game-map__middleground");
            that.surroundings.canvas_middleground_context = that.surroundings.canvas_middleground.getContext('2d');
            that.surroundings.canvas_middleground.width  = 960;     
            that.surroundings.canvas_middleground.height = 576;   
            
            that.surroundings.canvas_frontground =  document.getElementById("game-map__frontground");
            that.surroundings.canvas_frontground_context = that.surroundings.canvas_frontground.getContext('2d');
            that.surroundings.canvas_frontground.width  = 960;     
            that.surroundings.canvas_frontground.height = 576;   
        },
        
        render: function(){
            View.__super__.render.call(this);
            var that = this;

            that.get_canvas();
            that.gameField = $(".game-map");
            that.gameField.click(function(event){
                that.onGameFieldClick(event);
            });

            this.player.on("isWait:change", function(){
                if(that.player.isWait){
                    $(".game-info").hide();
                    that.gameField.hide();              
                    $(".game-result").hide();
                } else {
                    $(".game-info").show();
                    that.gameField.show(); 
                    $(".game-result").hide();
                }
            });
            
            this.surroundings.listenTo(this.player, "playerPosition", function(_pos){
                _pos = JSON.parse(_pos);
                that.surroundings.pos_x = _pos.x;
                that.surroundings.pos_y = _pos.y;
                that.surroundings.trigger("newPlayerPosition");
            });
            
            $.get("/res/tilemap.json", " ", function(data){
                //console.log(data);
                that.surroundings.map = data;
                that.surroundings.trigger("mapIsLoad");
            });
            
            this.surroundings.listenTo(this.player, "loadEntities", function(entities){
                that.surroundings.entities = JSON.parse(entities);
                that.surroundings.trigger("entitiesIsLoad");
            });
            this.player.on("availableActions", function(availableActions){
                that.availableActions = JSON.parse(availableActions);
                if(that.availableActions.length != 0){
                    $(".pressZ").show(); 
                } else {
                    $(".pressZ").hide();
                }
            });
            this.player.on("flagStatus", function(flagStatus){
                flagStatus = JSON.parse(flagStatus);
                $(".pointsRed").html(flagStatus.commandRed);
                $(".pointsBlue").html(flagStatus.commandBlue);
                $(".captureTime").html(flagStatus.captureTime);
            });
            this.player.on("entityStatus", function(entityStatus){
                entityStatus = JSON.parse(entityStatus);
                $(".game-info-status-player").html(entityStatus.hitPoints);
                $(".game-info-status-players-target").html(entityStatus.targetsHitPoints);
            });
            this.player.on("gameResult", function(result, playerCommand){
                result = JSON.parse(result);
                $(".game-result__result-blue").html(result.CommandBlue);
                $(".game-result__result-red").html(result.CommandRed);
                $(".game-result__result-winner").html(result.winner);
                if(result.isTechnical){
                    $(".game-result__winner-status").html("Техническая победа");
                } else {
                    $(".game-result__winner-status").html(" ");
                }
                $(".game-result__player-team").html(playerCommand); 
                if(result.winner === playerCommand){
                    $(".game-result__player-result").html("You win!"); 
                } else {
                    $(".game-result__player-result").html("You lose!"); 
                }
                $(".game-info").hide();
                $(".game-result").show(); 
            });
            this.player.on("abilityStatus", function(abilityStatus){
                abilityStatus = JSON.parse(abilityStatus);
                abilityStatus.forEach(function(ability, i){
                    ++i;
                    if(i == 10) i = 0;
                    if(i == 11) return;
                    
                    $(".ability" + i + "-name").html(ability.name);
                    $(".ability" + i + "-time").html(ability.time);
                });
            });
        },
        
        onGameFieldClick: function(event){
            if(this.player.isWait || this.player.isGameComplite){
                return;
            }
            var w = this.gameField.width() / 15;
            var h = this.gameField.height()/ 9;
            this.player.setTarget(Math.floor(event.offsetX/w)+1, Math.floor(event.offsetY/h)+1);
        },
               
        show: function(){
            View.__super__.show.call(this);
            this.player.status({
                success: function(data){
                    if(data.isLogin != 'true'){
                        Backbone.history.navigate('login', true);
                    }
                }
            });
            this.player.startGame();
        },
        hide: function(){
            this.player.leaveGame();
            View.__super__.hide.call(this);
        },
        
        playerMove: function(ev){
            if(this.player.isWait || this.player.isGameComplite){
                return;
            }
            var sim = String.fromCharCode(ev.keyCode || ev.which || 0);
            var that = this;
            //console.log();
            switch(sim){
                case 'ц':
                case 'w':{
                    that.player.move("up");
                }; break;
                case 'ы':
                case 's':{
                    that.player.move("down");
                }; break;
                case 'ф':
                case 'a':{
                    that.player.move("left");
                }; break;
                case 'в':
                case 'd':{
                    that.player.move("right");
                }; break;
                case 'я':
                case 'z':{
                    if(that.availableActions.length != 0){
                        that.player.startCapture();
                    }
                }; break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':{
                    var abilityName = $(".ability" + sim + "-name").html();
                    that.player.ability(abilityName);
                }; break;

            }
        }
    });

    return new View({content: '.page-game'});
});