class NaiveSearch
  def self.search(string, query)
    #matches1 = []
    #i = 0
    #while i < string.length
    #  c = string[i..string.length].index(query)
    #  break if c.nil?
    #  i += c
    #  matches1 << i.to_s
    #  i += 1
    #end

    matches = []
    string.length.times do |i|
      if string[i..(i + query.length - 1)] == query
        matches << i.to_s
      end
    end

    matches
  end
end

return unless ARGV.any?

#NaiveSearch.search(File.read(ARGV.first), ARGV.last)
input = File.read(ARGV.first)
query = ARGV.last
naive_matches = NaiveSearch.search(input, query)
ba_matches = `./search-ba #{ARGV.first} #{query}`.split(' ')
kmp_matches = `./search-kmp #{ARGV.first} #{query}`.split(' ')

puts naive_matches == kmp_matches
puts naive_matches == ba_matches
