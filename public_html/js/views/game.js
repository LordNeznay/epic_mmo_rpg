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

    var canvas_map_height = 1152;
    var canvas_map_width = 1920;

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
            that.surroundings.canvas_background.width  = canvas_map_width;     
            that.surroundings.canvas_background.height = canvas_map_height;
            
            that.surroundings.canvas_middleground =  document.getElementById("game-map__middleground");
            that.surroundings.canvas_middleground_context = that.surroundings.canvas_middleground.getContext('2d');
            that.surroundings.canvas_middleground.width  = canvas_map_width;     
            that.surroundings.canvas_middleground.height = canvas_map_height;  

            //Слой буфера
            that.surroundings.canvas_middleground2 =  document.getElementById("game-map__middleground2");
            that.surroundings.canvas_middleground_context2 = that.surroundings.canvas_middleground2.getContext('2d');
            that.surroundings.canvas_middleground2.width  = canvas_map_width;     
            that.surroundings.canvas_middleground2.height = canvas_map_height;  
            
            that.surroundings.canvas_frontground =  document.getElementById("game-map__frontground");
            that.surroundings.canvas_frontground_context = that.surroundings.canvas_frontground.getContext('2d');
            that.surroundings.canvas_frontground.width  = canvas_map_width;     
            that.surroundings.canvas_frontground.height = canvas_map_height;   
        },
        
        render: function(){
            View.__super__.render.call(this);
            var that = this;

            that.get_canvas();
            that.gameField = $(".game-map");
            that.gameField.click(function(event){
                that.onGameFieldClick(event);
            });

            that.hide_game_info();
            that.gameField.show(); 
            that.gameField.hide();    
            this.please_wait = $(".please_wait");
            this.game_result = $(".game-result");
            this.game_result.hide();
            
            this.player.on("isWait:change", function(){
                if(that.player.isWait){
                    that.hide_game_info();
                    that.gameField.hide();              
                    that.game_result.hide();
                    that.please_wait.show();
                    that.surroundings.stopAnimation();
                } else if(!that.player.isWait){
                    that.show_game_info();
                    that.gameField.show(); 
                    that.game_result.hide();
                    that.please_wait.hide();
                    that.surroundings.startAnimation();
                }
            });
            
            this.surroundings.listenTo(this.player, "playerPosition", function(_pos){
                that.surroundings.newPosition(_pos.x, _pos.y);
                that.surroundings.trigger("newPlayerPosition");
            });
            
            $.get("/res/tilemap.json", " ", function(data){
                that.surroundings.map = data;
                that.surroundings.trigger("mapIsLoad");
            });
            
            this.surroundings.listenTo(this.player, "loadEntities", function(entities){
                that.surroundings.newEntities(entities);
            });
            
            this.pressZ = $(".pressZ");
            this.player.on("availableActions", function(availableActions){
                that.availableActions = availableActions;
                if(that.availableActions.length != 0){
                    that.pressZ.show(); 
                } else {
                    that.pressZ.hide();
                }
            });
            
            this.pointsRed = $(".pointsRed");
            this.pointsBlue = $(".pointsBlue");
            this.captureTime = $(".captureTime");
            this.player.on("flagStatus", function(flagStatus){
                that.pointsRed.html(flagStatus.commandRed);
                that.pointsBlue.html(flagStatus.commandBlue);
                that.captureTime.html(flagStatus.captureTime);
            });
            
            this.players_hitpoints__player = $(".players-hitpoints__player");
            this.players_hitpoints__player_target = $(".players-hitpoints__players-target");
            this.player.on("entityStatus", function(entityStatus){
                that.players_hitpoints__player.html(entityStatus.hp);
                that.players_hitpoints__player_target.html(entityStatus.thp);
            });
            
            this.game_result__result_blue = $(".game-result__result-blue");
            this.game_result__result_red = $(".game-result__result-red");
            this.game_result__result_winner = $(".game-result__result-winner");
            this.game_result__winner_status = $(".game-result__winner-status");
            this.game_result__player_team = $(".game-result__player-team");
            this.game_result__player_result = $(".game-result__player-result");
            this.game_result = $(".game-result");
            this.player.on("gameResult", function(result, playerCommand){
                result = JSON.parse(result);
                that.game_result__result_blue.html(result.CommandBlue);
                that.game_result__result_red.html(result.CommandRed);
                that.game_result__result_winner.html(result.winner);
                if(result.isTechnical){
                    that.game_result__winner_status.html("Техническая победа");
                } else {
                    that.game_result__winner_status.html(" ");
                }
                that.game_result__player_team.html(playerCommand); 
                if(result.winner === playerCommand){
                    that.game_result__player_result.html("You win!"); 
                } else {
                    that.game_result__player_result.html("You lose!"); 
                }
                that.hide_game_info();
                that.game_result.show(); 
            });
            this.player.on("abilityStatus", function(abilityStatus){
                abilityStatus.forEach(function(ability, i){
                    ++i;
                    if(i == 10) i = 0;
                    if(i == 11) return;
                    
                    $(".ability__name_number-"+i).html(ability.name);
                    $(".ability__time_number-"+i).html(ability.time);
                });
            });
            
            
            
            //Для адаптивного внешнего вида
            that.canv = $('canvas');
            that.gm = $('.game-map');
            that.ab = $('.ability');
            that.abs = $('.abilities-status');
            that.gi = $('.game-info');
            that.fi = $('.game-info-status-flags');
            that.body = $('body');
            that.resize_canvas();
            $(window).resize(function(){
                that.resize_canvas()
            });
        },
        
        hide_game_info: function(){
            $(".game-info").hide();
        },
        
        show_game_info: function(){
            $(".game-info").show();
        },
        
        resize_canvas: function(){
            var that = this;
            var w = that.body.width();
            var h = document.documentElement.clientHeight;
            if(w/h >= 1.25){
                that.gm.width(h/12  *15);
                that.gm.height(9/12 * h);
                that.canv.width(h/12  *15);
                that.canv.height(9/12 * h);
                
                that.abs.height(1.8/12*h);
                that.abs.width(h/12*15);
                
                that.gi.width(h/12*15);
                
                that.ab.height(1.8/12*h);
                that.ab.width(h/12*15/10-1.2);
                
                that.gm.css('margin-left', (w-h/12*15)/2);
                that.gi.css('margin-left', (w-h/12*15)/2);
            } else {
                that.gm.width(w);
                that.gm.height(w/15 * 9);
                that.canv.width(w);
                that.canv.height(w/15 * 9);
                
                that.ab.height(w/15 * 1.8);
                that.ab.width(w/10-1.2);

                that.abs.height(1.8/12*h);
                that.abs.width(w);
                
                that.gi.width(h/12*15);
                
                that.gm.css('margin-left', 0);
                that.gi.css('margin-left', 0);
            }
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
            this.surroundings.stopAnimation();
            View.__super__.hide.call(this);
        },
        
        playerMove: function(ev){
            if(this.player.isWait || this.player.isGameComplite){
                return;
            }
            var sim = String.fromCharCode(ev.keyCode || ev.which || 0);
            var that = this;

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
                    var abilityName = $(".ability__name_number-"+sim).html();
                    that.player.ability(abilityName);
                }; break;

            }
        }
    });

    return new View({content: '.page-game'});
});