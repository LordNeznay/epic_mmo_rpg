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
        className: "game-view",
        
        surroundings: new Surroundings(),
        
        events: {
            "click a": "hide",
        },
        child_init: function(){
            _.bindAll(this, 'playerMove');
            $(document).bind('keypress', this.playerMove);
        },
        child_render: function(){
            var that = this;
            that.surroundings.canvas =  document.getElementById("game-map__canvas");
            that.surroundings.gameField = that.surroundings.canvas.getContext('2d');
            
            that.surroundings.canvas.width  = 960;     
            that.surroundings.canvas.height = 576;
            
            that.surroundings.canvas.onclick = function(event){
                that.onGameFieldClick(event);
            };

            this.surroundings.listenTo(this.player, "loadMap", function(_map){
                that.surroundings.map = JSON.parse(_map);
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
        },
        
        onGameFieldClick: function(event){
            this.player.setTarget(Math.floor(event.offsetX/64)+1, Math.floor(event.offsetY/64)+1);
        },
        
        child_show: function(){
            this.player.status({
                success: function(data){
                    if(data.isLogin != 'true'){
                        Backbone.history.navigate('login', true);
                    }
                }
            });
            this.player.startGame();
        },
        child_hide: function(){
            this.player.leaveGame();
        },
        
        playerMove: function(ev){
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
                case '1':{
                    that.player.ability1();
                }; break;
                case '2':{
                    that.player.ability2();
                }; break;
            }
        }
    });

    return new View({content: '.page-game'});
});