def time
  start = Time.now
  yield
  return Time.now - start
end

return unless ARGV.any?

File.open(ARGV.first, "r") do |infile|
  all_read = 0
  loop do
    read = infile.read 5000
    break if read.nil?
    all_read += read.length

    File.open('evaluate.tmp', "a") do |tempfile|
      tempfile.write read
    end

    t = 0
    10.times do
      t += time do
        `./tandem evaluate.tmp`
      end
    end
    puts all_read.to_s + ' ' + (t / 10).to_s
  end

  File.delete 'evaluate.tmp'
end