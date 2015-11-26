define([
    'backbone',
    'underscore',
    'views/base',
    'tmpl/scoreboard',
    'collections/scores'
], function(
    Backbone,
    Underscore,
    BaseView,
    tmpl,
    playerCollection
){

    var View = BaseView.extend({
        name: 'scoreboard',
        template: tmpl,
        collection: playerCollection,
		
        initialize : function(options) {
            View.__super__.initialize.call(this, {content: '.page-scoreboard'});
        },
        
        events: {
            "click a": "hide"
        },
        
        render: function () {
            //View.__super__.render.call(this);
            var that = this;

            that.collection.on('loadScoreboard', function(players){
                that.$el.append(that.template(players));
                if(window.location.hash == '#scoreboard'){
                    that.show();
                }
            });

        },
        //show: function(){
            //View.__super__.show.call(this);
            //this.collection.fetch();
        //}

    });

    return new View({content: '.page-scoreboard'});
});