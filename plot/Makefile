all: output.png output-normalized.png

output.png: commands.gnuplot
	gnuplot < commands.gnuplot

output-normalized.png: commands.gnuplot
	gnuplot < commands.gnuplot

clean:
	rm output.png output-normalized.png
