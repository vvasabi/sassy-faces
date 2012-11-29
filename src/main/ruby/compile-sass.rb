require 'rubygems'
require 'sass'
require 'sass/plugin'

# Compile the given SASS code and print to stdout
Sass::Engine.new($input,
	:style => $style.nil? ? :compressed : eval(':' + $style),
	:syntax => $syntax.nil? ? :sass : eval(':' + $syntax)
).render
