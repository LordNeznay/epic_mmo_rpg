define([
    'backbone',
    'tmpl/surroundings'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        map: '',
        pos_x: 0,
        pos_y: 0,
        entities: '',
        template: tmpl,
        canvas: undefined,
        gameField: undefined,
        loadTilesets: 0,
        amountTilesets: 0,

        initialize: function(){
            this.on("mapIsLoad", this.loadingTilesets, this);
            this.on("newPlayerPosition", this.redrawMap, this);
            this.on("entitiesIsLoad", this.drawEntities, this);
            this.on("readyDrawMap", this.drawMap, this);
        },

        setMap: function(map){
            this.map = map;
        },
        
        drawTile: function(gid, x, y){
            var that = this;
            
            //if(gid == 0) return;
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
        
        loadingTilesets: function(){
            var dop_tileset = {
                firstgid: -1,
                image: "0.png",
                imageheight: 64, 
                imagewidth: 64, 
                name: "zeroTile",
                tileheight: 64, 
                tilewidth: 64
            }
            this.map.tilesets.push(dop_tileset);
            
            var that = this;
            var amount = 0;
            that.loadTilesets = 0;
            //console.log(this.map);
            this.map.tilesets.forEach(function(tileset){
                amount += 1;
                var pic = new Image();
                pic.src    = 'http://' + document.location.host + '/res/' + tileset.image;
                pic.onload = function() { 
                    tileset.image = pic;
                    that.loadTilesets++;
                    that.trigger("readyDrawMap");
                    //alert("x=" + amount);
                }
            });
            this.amountTilesets = amount;
        },
        
        drawEntities: function(){
            var that = this;
            this.entities.entities.forEach(function(entity){
                var pic = new Image();
                pic.src = 'http://' + document.location.host + '/res/' + entity.image;
                pic.onload = function(){
                    that.gameField.drawImage(pic, (entity.x-1) * 64, (entity.y-1) * 64);
                }
            });
            
            var pic = new Image();
            pic.src = 'http://' + document.location.host + '/res/' + this.entities.player.image;
            pic.onload = function(){
                that.gameField.drawImage(pic, (that.entities.player.x-1) * 64, (that.entities.player.y-1) * 64);
            }
        },
        
        drawMap: function(){
            //Если картинки не догружены, то выйти
            //alert(this.loadTilesets);
            if(this.amountTilesets != this.loadTilesets) return;
            
            var that = this;
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
                    while(x >= (that.map.width-1) * dx){
                        x = -dx;
                        y += dy;
                    }
                });
            });
        },
        
        getTileGID: function(x, y, layer){
            if(x >= this.map.width || x < 0) return -1;
            if(y >= this.map.height || y < 0) return -1;
            return layer.data[y * this.map.width + x];
        },
        
        redrawMap: function(){
            //Если картинки не догружены, то выйти
            if(this.amountTilesets != this.loadTilesets) return;
            var that = this;
            
            that.map.layers.forEach(function(layer){
                if(!(layer.name == "Background" || layer.name == "Frontground")){
                    return;
                }
                var dx = that.map.tilewidth;
                var dy = that.map.tileheight;
                
                for(var j = that.pos_y - 5; j <= that.pos_y + 5; ++j){
                    for(var i = that.pos_x - 8; i <= that.pos_x + 8; ++i){
                        that.drawTile(that.getTileGID(i, j, layer), (i - that.pos_x + 7)*dx, (j - that.pos_y + 4)*dy);
                    }
                }
            });
        }
        
    });

    return View;
});