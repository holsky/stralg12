class NaiveSearch
  def self.search(string, query)
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

input = File.read(ARGV.first)
failed = false
1000.times do |i|
  q_from = Random.rand(input.length - 17)
  q_until = q_from + Random.rand(15) + 2
  query = input[q_from..q_until]
  #puts query
  naive_matches = NaiveSearch.search(input, query)
  ba_matches = `./search-ba #{ARGV.first} #{query}`.split(' ')
  kmp_matches = `./search-kmp #{ARGV.first} #{query}`.split(' ')

  failed = true unless naive_matches == kmp_matches
  failed = true unless naive_matches == ba_matches
  break if failed
end

puts "FAIL" if failed
puts "OK" unless failed
