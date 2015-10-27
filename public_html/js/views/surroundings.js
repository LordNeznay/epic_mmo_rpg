define([
    'backbone',
    'tmpl/surroundings'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        map: '',
        template: tmpl,
        canvas: undefined,
        gameField: undefined,
        loadTilesets: 0,

        initialize: function(){
            this.on("readyDraw", this.drawMap, this);
        },

        setMap: function(map){
            this.map = map;
        },
        
        drawMap: function(){
            var that = this;
            var x = 0;
            this.map.tilesets.forEach(function(tileset){
                var pic = new Image();
                pic.src    = 'http://' + document.location.host + '/res/' + tileset.image;
                pic.onload = function() { 
                    tileset.image = pic;
                    that.loadTilesets++;
                    that.gameField.drawImage(tileset.image, x, 0);
                    x += tileset.imagewidth;
                }
            });
        }
        
    });

    return View;
});