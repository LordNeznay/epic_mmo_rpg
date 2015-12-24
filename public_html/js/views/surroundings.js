define([
    'backbone',
    'tmpl/game'
], function(
    Backbone,
    tmpl
){

    var canvas_map_height = 576;
    var canvas_map_width = 960;
    var canvas_tile_width = 64;
    var canvas_tile_height = 64;


    var View = Backbone.View.extend({
        map: '',
        pos_x: 0,
        pos_y: 0,
        entities: '',
        template: tmpl,
        canvas_background: undefined,
        canvas_background_context: undefined,
        canvas_middleground: undefined,
        canvas_middleground_context: undefined,
        canvas_frontground: undefined,
        canvas_frontground_context: undefined,
        
        offsetX: 0,
        offsetY: 0,
        directMove: "none",
        fps: 30,
        sfps: 5,
        dst: 0,
        minD: 0.5,
        
        loadTilesets: 0,
        amountTilesets: 0,

        initialize: function(){
            this.on("mapIsLoad", this.loadingTilesets, this);
            this.on("newPlayerPosition", this.updMap, this);
            this.on("entitiesIsLoad", this.drawEntities, this);
            this.on("readyDrawMap", this.drawMap, this);
        },
        
        updMap: function(){
            //Функция вызовится при сдвиге карты.
            this.redrawMap();
        },
        
        newPosition: function(x, y){
            var that = this;
            if((x > that.pos_x && y > that.pos_y) || (x < that.pos_x && y < that.pos_y)
                || ((x - that.pos_x) > 1) || ((x - that.pos_x) < -1) || ((y - that.pos_y) > 1) || ((y - that.pos_y) < -1)
                ){
                
            } else {
                if(x > that.pos_x && y == that.pos_y){
                    that.startMove("right");
                    that.offsetX = canvas_tile_width;
                    that.offsetY = 0;
                }
                if(x < that.pos_x && y == that.pos_y){
                    that.startMove("left");
                    that.offsetX = -canvas_tile_width;
                    that.offsetY = 0;
                }
                if(x == that.pos_x && y > that.pos_y){
                    that.startMove("down");
                    that.offsetX = 0;
                    that.offsetY = canvas_tile_width;
                }
                if(x == that.pos_x && y < that.pos_y){
                    that.startMove("up");
                    that.offsetX = 0;
                    that.offsetY = -canvas_tile_width;
                }
            }
            that.pos_x = x;
            that.pos_y = y;
        },
        
        startAnimation: function(){
            var that = this;
            this.timerId = setInterval(function(){
                that.step();
            }, 1000/that.fps);
        },
        
        stopAnimation: function(){
            clearInterval(this.timerId);
        },
        
        stopMove: function(){
            this.offsetX = 0;
            this.offsetY = 0;
            this.directMove = "none";
            this.dst = 0;
        },

        startMove: function(direct){
            this.dst = 0;
            this.directMove = direct;
        },
        
        step: function(){
            var that = this;
            var dist = canvas_tile_width*that.sfps/that.fps;
            this.dst += dist;
            switch(this.directMove){
                case "up": {
                    that.offsetY += dist;
                }; break;
                case "down": {
                    that.offsetY -= dist;
                }; break;
                case "left": {
                    that.offsetX += dist;
                }; break;
                case "right": {
                    that.offsetX -= dist;
                }; break;
            }
            
            if(Math.abs(this.offsetX) < this.minD && Math.abs(this.offsetY) < this.minD){
                this.stopMove();
            }
            
            if(this.dst > canvas_tile_width){
                this.stopMove();
            }
            this.redrawMap();
            that.trigger("entitiesIsLoad");
        },
        
        setMap: function(map){
            this.map = map;
        },
        
        drawTile: function(gid, x, y){
            var that = this;
            
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
                    that.canvas_background_context.drawImage(tileset.image, xx, yy, tileset.tilewidth, tileset.tileheight, x + that.offsetX, y + that.offsetY, tileset.tilewidth, tileset.tileheight);
                }
            });
        },
        
        loadingTilesets: function(){
            var dop_tileset = {
                firstgid: -1,
                image: "0.png",
                imageheight: canvas_tile_height, 
                imagewidth: canvas_tile_width, 
                name: "zeroTile",
                tileheight: canvas_tile_height, 
                tilewidth: canvas_tile_width
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
            this.canvas_middleground.width  = canvas_map_width;     
            this.canvas_middleground.height = canvas_map_height;  
        
            var that = this;
            this.entities.entities.forEach(function(entity){
                var pic = new Image();
                pic.src = 'http://' + document.location.host + '/res/' + entity.image;
                pic.onload = function(){
                    that.canvas_middleground_context.drawImage(pic, (entity.x-1) * canvas_tile_width  + that.offsetX, (entity.y-1) * canvas_tile_height  + that.offsetY);
                }
            });
            
            var pic = new Image();
            pic.src = 'http://' + document.location.host + '/res/' + this.entities.player.image;
            pic.onload = function(){
                that.canvas_middleground_context.drawImage(pic, (that.entities.player.x-1) * canvas_tile_width, (that.entities.player.y-1) * canvas_tile_height);
            }
        },
        
        drawMap: function(){
            //Если картинки не догружены, то выйти
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
                    that.drawTile(gid, x  + that.offsetX, y  + that.offsetY);
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
            //Отменить движение
            //this.stopMove();
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
            //alert("1");
        }
        
    });

    return View;
});