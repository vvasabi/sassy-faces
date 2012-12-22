require 'rubygems'
require 'sass'
require 'sass/plugin'

if $compass
  begin
    spec = Gem::Specification.find_by_name('compass')
    $load_paths += [spec.gem_dir + '/frameworks/blueprint/stylesheets']
    $load_paths += [spec.gem_dir + '/frameworks/compass/stylesheets']
  rescue LoadError
    raise 'Please make sure the Compass gem is on the class path.'
  end
end

# Compile the given SASS code and print to stdout
Sass::Engine.new($input,
                 :cache => !$cache_location.nil?,
                 :cache_location => $cache_location,
                 :style => $style.nil? ? :compressed : eval(':' + $style),
                 :syntax => $syntax.nil? ? :sass : eval(':' + $syntax),
                 :load_paths => $load_paths
).render
