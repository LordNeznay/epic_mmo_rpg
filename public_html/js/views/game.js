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
            $(".game-result").hide();
            
            this.player.on("isWait:change", function(){
                if(that.player.isWait){
                    that.hide_game_info();
                    that.gameField.hide();              
                    $(".game-result").hide();
                    $(".please_wait").show();
                    that.surroundings.stopAnimation();
                } else if(!that.player.isWait){
                    that.show_game_info();
                    that.gameField.show(); 
                    $(".game-result").hide();
                    $(".please_wait").hide();
                    that.surroundings.startAnimation();
                }
            });
            
            this.surroundings.listenTo(this.player, "playerPosition", function(_pos){
                //that.surroundings.pos_x = _pos.x;
                //that.surroundings.pos_y = _pos.y;
                that.surroundings.newPosition(_pos.x, _pos.y);
                that.surroundings.trigger("newPlayerPosition");
            });
            
            $.get("/res/tilemap.json", " ", function(data){
                that.surroundings.map = data;
                that.surroundings.trigger("mapIsLoad");
            });
            
            this.surroundings.listenTo(this.player, "loadEntities", function(entities){
                //that.surroundings.entities = entities;
                that.surroundings.newEntities(entities);
                //that.surroundings.trigger("entitiesIsLoad");
            });
            this.player.on("availableActions", function(availableActions){
                that.availableActions = availableActions;
                if(that.availableActions.length != 0){
                    $(".pressZ").show(); 
                } else {
                    $(".pressZ").hide();
                }
            });
            this.player.on("flagStatus", function(flagStatus){
                $(".pointsRed").html(flagStatus.commandRed);
                $(".pointsBlue").html(flagStatus.commandBlue);
                $(".captureTime").html(flagStatus.captureTime);
            });
            this.player.on("entityStatus", function(entityStatus){
                $(".players-hitpoints__player").html(entityStatus.hp);
                $(".players-hitpoints__players-target").html(entityStatus.thp);
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
                that.hide_game_info();
                $(".game-result").show(); 
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
                //that.abs.css('margin-left', (w-h/12*15)/2);
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
                //that.abs.css('margin-left', 0);
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
            //console.log();
            switch(sim){
                case 'ц':
                case 'w':{
                    that.player.move("up");
                    //that.surroundings.startMove("up");
                }; break;
                case 'ы':
                case 's':{
                    that.player.move("down");
                    //that.surroundings.startMove("down");
                }; break;
                case 'ф':
                case 'a':{
                    that.player.move("left");
                    //that.surroundings.startMove("left");
                }; break;
                case 'в':
                case 'd':{
                    that.player.move("right");
                    //that.surroundings.startMove("right");
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