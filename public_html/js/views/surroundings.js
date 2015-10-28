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
        
        drawTile: function(gid, x, y){
            var that = this;
            
            if(gid == 0) return;
            that.map.tilesets.forEach(function(tileset){
                if(tileset.firstgid <= gid && gid < tileset.firstgid + (tileset.imageheight*tileset.imagewidth/tileset.tileheight/tileset.tilewidth)){
                    var dgid = gid - tileset.firstgid;
                    var xx = 0;
                    var yy = 0;
                    xx = dgid * tileset.tilewidth;
                    while(xx >= tileset.imagewidth){
                        yy += tileset.tileheight;
                        xx -= tileset.imagewidth;
                    }
                    that.gameField.drawImage(tileset.image, xx, yy, tileset.tilewidth, tileset.tileheight, x, y, tileset.tilewidth, tileset.tileheight);
                }
            });
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
                    //that.gameField.drawImage(tileset.image, x, 0);
                    x += tileset.imagewidth;
                }
            });
            
            setTimeout(function(){
            //that.drawTile(1, -63, -32);

            that.map.layers.forEach(function(layer){
                if(!(layer.name == "Background" || layer.name == "Frontground")){
                    return;
                }
                var dx = that.map.tilewidth;
                var dy = that.map.tileheight;
                var x = -dx;
                var y = -dy;
                layer.data.forEach(function(gid){
                    that.drawTile(gid, x, y);
                    x += dx;
                    while(x >= (layer.width-1) * dx){
                        x = -dx;
                        y += dy;
                    }
                });
            });
            }, 1000);
            /*
drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
// Первый параметр указывает на изображение
// sx, sy, sWidth, sHeight указывают параметры фрагмента на изображение-источнике
// dx, dy, dWidth, dHeight ответственны за координаты отрисовки фрагмента на холсте
            */
        }
        
    });

    return View;
});