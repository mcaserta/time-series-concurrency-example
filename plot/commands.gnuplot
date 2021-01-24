# see https://bhoey.com/blog/simple-time-series-graphs-with-gnuplot/
# for more info
set xdata time                           # Indicate that x-axis values are time values
set xlabel "time"                        # Sets the label for the x-axis
set timefmt "%H:%M:%S"                   # Indicate the pattern the time values will be in
set format x "%H:%M:%S"                  # Set how the dates will be displayed on the plot
 
set xrange ["08:01:00":"08:09:00"]       # Set x-axis range of values
set yrange [0:25]                        # Set y-axis range of values
 
set key off                              # Turn off graph legend
set xtics rotate by -45                  # Rotate dates on x-axis 45deg for cleaner display
set title 'Air Quality'                  # Set graph title
set grid                                 # Use a grid in the background
set key opaque                           # Without this the labels won't show up
 
set terminal png                         # Set the output format to png
set output 'output.png'                  # Set output file to output.png
 
plot 'time-series.dat' using 1:2 with linespoints lw 3 t 'Air Quality Index', \
     'time-series.dat' using 1:3 with linespoints lw 3 t 'Temperature C°', \
     'time-series.dat' using 1:4 with linespoints lw 3 t 'Carbon Monoxide Percentage'

set output 'output-normalized.png'       # Set output file to output-normalized.png

plot 'time-series-normalized.dat' using 1:2 with linespoints lw 3 t 'Air Quality Index', \
     'time-series-normalized.dat' using 1:3 with linespoints lw 3 t 'Temperature C°', \
     'time-series-normalized.dat' using 1:4 with linespoints lw 3 t 'Carbon Monoxide Percentage'
