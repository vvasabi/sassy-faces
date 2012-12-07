require 'rubygems'
require 'sass/importers'
require 'java'
java_import com.bc.sass.SassImporterFactory

# JRubyImporter that dispatches import responsibility to Java
class JRubyImporter < Sass::Importers::Base

  attr_accessor :root
  attr_accessor :java

  # Creates a new filesystem importer that imports files relative to a given
  # path.
  #
  # @param root [String] The root path.
  #   This importer will import files relative to this path.
  def initialize(root)
    factory = SassImporterFactory.get_instance()
    @root = root
    @java = factory.create_sass_importer(root)
  end

  # @see Base#find_relative
  def find_relative(name, base, options)
    file = java.findRelative(name, base, options)
    _make_engine(file, options)
  end

  # @see Base#find
  def find(name, options)
    file = java.find(name, options)
    _make_engine(file, options)
  end

  def _make_engine(file, options)
    return if file.nil?

    options[:syntax] = eval(':' + file.syntax.toString())
    options[:filename] = file.filename
    options[:importer] = self
    Sass::Engine.new(file.file_content, options)
  end

  # @see Base#to_s
  def to_s
    @root
  end
end
