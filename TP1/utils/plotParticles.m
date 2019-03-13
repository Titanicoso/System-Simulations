function plotParticles(particle, neighbours, points, data)
   
   hold on;
   
   scatter(points(particle,1), points(particle, 2), 'r');
   
   for i = (neighbours(particle, :))
     if(i != 0)
      scatter(points(i,1), points(i, 2), 'g');
     endif
   endfor
   
   for i = 1 : rows(points)
     if (i!= 0 && i != particle && !any(neighbours(particle, :) == i))
       scatter(points(i,1), points(i, 2), 'c');
     endif
   endfor
   
   axis([0 data(1) 0 data(1)])
   
   hold off;
  
endfunction