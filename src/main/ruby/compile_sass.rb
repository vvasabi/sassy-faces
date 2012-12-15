require 'rubygems'
require 'sass'
require 'sass/plugin'

# Compile the given SASS code and print to stdout
Sass::Engine.new($input,
                 :cache => false,
                 :style => $style.nil? ? :compressed : eval(':' + $style),
                 :syntax => $syntax.nil? ? :sass : eval(':' + $syntax),
                 :load_paths => $load_paths
).render
