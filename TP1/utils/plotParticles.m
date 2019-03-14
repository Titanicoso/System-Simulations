function plotParticles(particle, neighbours, points, data)
   
   hold on;
   
   t = linspace(0,2*pi,25)';
   r = data(3);
   x = r.*cos(t) + points(particle, 1);
   y = r.*sin(t) + points(particle, 2);
   fill(x,y,'r');
   %scatter(points(particle,1), points(particle, 2), 'r');
   
   if(columns(neighbours) != 0)
     for i = (neighbours(particle, :))
       if(i != 0)
        x = r.*cos(t) + points(i, 1);
        y = r.*sin(t) + points(i, 2);
        fill(x,y,'g');
        %scatter(points(i,1), points(i, 2), 'g');
       endif
     endfor
   endif
   
   for i = 1 : rows(points)
     if (i!= 0 && i != particle && ((columns(neighbours) == 0) || !any(neighbours(particle, :) == i)))
      x = r.*cos(t) + points(i, 1);
      y = r.*sin(t) + points(i, 2);
      fill(x,y,'c');
       %scatter(points(i,1), points(i, 2), 'c');
     endif
   endfor
   
   axis([0 data(1) 0 data(1)])
   set(gca,'xtick',[0:(data(1)/data(4)):data(1)]);
   set(gca,'ytick',[0:(data(1)/data(4)):data(1)]);
   grid on;
   
   hold off;
  
endfunction