define([
    'backbone',
    'tmpl/game',
    'underscore',
    'views/base',
    'models/cell'
], function(
    Backbone,
    tmpl,
    Underscore,
    BaseView,
    CellModel
){

    //Создание видимой части карты
    var cells_b = [];
    var cells_f = [];
    var i = 0;
    var j = 0;
    for(i=0; i<15; ++i){
        for(j=0; j<9; ++j){
            cells_b.push(new CellModel({view_x: i, view_y: j, index_z: 1}));
            cells_f.push(new CellModel({view_x: i, view_y: j, index_z: 10}));
        }
    }

    var View = BaseView.extend({
        name: 'game',
        template: tmpl,
        className: "game-view",
        tilemap_xml: "",
        isMapLoad: false,
        isMapParse: false,
        cells_b: cells_b,
        cells_f: cells_f,
        view_point_x: 8,
        view_point_y: 5,
        widthMap: 0,
        heightMap: 0,

        child_init: function(){
            _.bindAll(this, 'move');
            $(document).bind('keypress', this.move);
            var that = this;
            //Загружаем карту
            $.ajax({
                type: "GET",
                datatype: "xml",
                url: "/res/tilemap.tmx",
                success: function(data){
                    that.tilemap_xml = $.parseXML(data);
                    that.isMapLoad = true;
                }
            });
            //Загружаем шаблон ячейки
            $.ajax({url: "utils/field_cell.html",
                context: this,
                success: function(response) {
                    this.cellTemplate = response;
                    this.showGameField();
                }
            });
        },
        events: {
            "click a": "hide",
        },
        showGameField: function(){
            while(!this.isMapParse){
                if(!this.isMapLoad) continue;
                this.mapParse();
            }
            $('.game-map').html(Underscore.template(this.cellTemplate, {cells: cells_b})); 
            $('.game-map').append(Underscore.template(this.cellTemplate, {cells: cells_f})); 
        },
        mapParse: function(){
            var that = this;
            var i = 0;
            var j = 0; 
            //Обновляем мировые координаты для отображаемых ячеек
            that.cells_b.forEach(function(element, k){
                that.cells_b[k].set('x', that.view_point_x -7 + i);
                that.cells_b[k].set('y', that.view_point_y -4 + j);
                that.cells_f[k].set('x', that.view_point_x -7 + i);
                that.cells_f[k].set('y', that.view_point_y -4 + j);
                ++j;
                if(j>8){
                    i++;
                    j = 0;
                }
            });  
            this.isMapParse = true;
            var map = $(this.tilemap_xml).find("map");
            var width = map.attr('width');
            var height = map.attr('height');
            this.widthMap = width;
            this.heightMap = height;
            var layerBackground = map.find("layer[name='Background']");
            var layerFrontground = map.find("layer[name='Frontground']");

            //Обновляем GID для заднего фона
            i=0;j=0;
            $(layerBackground).find("tile").each(function(){
                if(i==width){
                    i = 0;
                    ++j;
                }
                //console.log(i);
                if(i >= that.view_point_x-7 && i <= that.view_point_x+7 && j >= that.view_point_y-4 && j <= that.view_point_y+4){
                    var g = $(this).attr('gid');
                    that.cells_b.forEach(function(element, k){
                        if(that.cells_b[k].get('x') === i && that.cells_b[k].get('y') === j){
                            that.cells_b[k].set('gid', g);
                        }
                    });
                }
                ++i;
            });
            
            //Обновляем GID для переднего фона
            i=0;j=0;
            $(layerFrontground).find("tile").each(function(){
                if(i==width){
                    i = 0;
                    ++j;
                }
                //console.log(i);
                if(i >= that.view_point_x-7 && i <= that.view_point_x+7 && j >= that.view_point_y-4 && j <= that.view_point_y+4){
                    var g = $(this).attr('gid');
                    g = g==0 ? '00' : g;
                    that.cells_f.forEach(function(element, k){
                        if(that.cells_f[k].get('x') === i && that.cells_f[k].get('y') === j){
                            that.cells_f[k].set('gid', g);
                        }
                    });
                }
                ++i;
            });
        },
        move: function(ev){
            var sim = String.fromCharCode(ev.keyCode || ev.which || 0);
            var that = this;
            //console.log();
            switch(sim){
                case 'ц':
                case 'w':{
                    if(that.view_point_y-1 >= 4){
                        that.view_point_y--;
                    }
                    this.mapParse();
                    this.showGameField();
                }; break;
                case 'ы':
                case 's':{
                    if(that.view_point_y+1 < that.heightMap - 4){
                        that.view_point_y++;
                    }
                    this.mapParse();
                    this.showGameField();
                }; break;
                case 'ф':
                case 'a':{
                    if(that.view_point_x-1 >= 7){
                        that.view_point_x--;
                    }
                    this.mapParse();
                    this.showGameField();
                }; break;
                case 'в':
                case 'd':{
                    if(that.view_point_x+1 < that.widthMap - 7){
                        that.view_point_x++;
                    }
                    this.mapParse();
                    this.showGameField();
                }; break;
            }
        }

    });

    return new View({content: '.page-game'});
});