module.exports = function (grunt) {

    grunt.initConfig({
        shell: {
            options: {
                stdout: true,
                stderr: true
            },
            server: {
                command: 'java -cp ./classes/artifacts/L1_2_jar/L1.2.jar main.Main 8080'
            }
        },
        fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'templates',
                    src: '*.xml',
                    dest: 'public_html/js/tmpl'
                }],
                options: {
                    template: function (data) {
                        return grunt.template.process(
                            'define(function () { return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
            }
        },
        watch: {
            fest: {
                files: ['templates/*.xml'],
                tasks: ['fest'],
                options: {
                    interrupt: true,
                    atBegin: true
                }
            },
            server: {
                files: [
                    'public_html/js/**/*.js',
                    'public_html/css/**/*.css'
                ],
                options: {
                    livereload: true
                }
            },
            sass: {
                files: ['public_html/css/scss/*.scss'],
                tasks: ['sass:dev'],
            },
            css: {
                files: ['public_html/css/gen-css/*.css'],
                tasks: ['concat:css'],
            }
        },
        concurrent: {
            target: ['watch', 'shell'],
            options: {
                logConcurrentOutput: true
            }
        },
        
        sass: {
            dev: {
                options: {
                    style: 'expanded'
                },
                files: [{
                    expand: true,
                    cwd: 'public_html/css/scss',
                    src: '*.scss',
                    dest: 'public_html/css/gen-css',
                    ext: '.css'
                }]
            }
        },
        
        concat: {
            map: {
                src: ['src/main/resources/data/tilemap.json'],
                dest: 'public_html/res/tilemap.json',
            },
            css: {
                src: ['public_html/css/gen-css/*.css'],
                dest: 'public_html/css/main.css',
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask('default', ['sass:dev', 'concat',  'concurrent']);

};