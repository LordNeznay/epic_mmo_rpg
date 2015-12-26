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
                tasks: ['concat:css', 'cssmin'],
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
            },
            
            build: {
				separator: ';\n',
				src: [
					'public_html/js/lib/almond.js',
					'public_html/js/build/main.js'
				],
				dest: 'public_html/js/build.js'
			},
            
            forProd: {
                src: ['public_html/index/index_prod.html'],
                dest: 'production/public_html/index.html',
            },
            
            forDev: {
                src: ['public_html/index/index_dev.html'],
                dest: 'public_html/index.html',
            },
            
            mapForProd: {
                src: ['src/main/resources/data/tilemap.json'],
                dest: 'production/public_html/res/tilemap.json',
            },
            
            config: {
                src: ['src/main/resources/cfg/config.properties'],
                dest: 'production/src/main/resources/cfg/config.properties',
            }
        },
        
        cssmin: {
            dev: {
                files: [{
                    expand: true,
                    cwd: 'public_html/css',
                    src: ['*.css', '!*.min.css'],
                    dest: 'public_html/css',
                    ext: '.min.css'
                }]
            },
            prod: {
                files: [{
                    expand: true,
                    cwd: 'public_html/css',
                    src: ['*.css', '!*.min.css'],
                    dest: 'production/public_html/css',
                    ext: '.min.css'
                }]
            }        
        },
        
        requirejs: {
			build: {
				options: {
					almond: true,
					baseUrl: 'public_html/js',
                    mainConfigFile: 'public_html/js/main.js',
					name: 'main',
					optimize: 'none',
					out: 'public_html/js/build/main.js'
				}
			}
		},
        uglify: { 
			build: {
				files: {
					'production/public_html/js/build.min.js': ['public_html/js/build.js']
				}
			}
		}
    });

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    
    grunt.loadNpmTasks('grunt-contrib-requirejs');
	grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.registerTask('default', ['concat:forDev', 'sass:dev', 'concat:css', 'cssmin:dev', 'concurrent']);
    grunt.registerTask('build', ['concat:forProd', 'sass:dev', 'requirejs', 'concat:css', 'concat:build', 'cssmin:prod', 'uglify']);

};