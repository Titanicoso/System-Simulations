function [msd , position, collisions, t0, t3] = parser()
   
  data = dlmread('../MSD.data', ' ', 0 , 0);
  data2 = dlmread('../collision_times.data', ' ', 0 , 0);
  data3 = dlmread('../velocity_modules.data', ' ', 0 , 0);
  
  msd = data(:,1:2);
  position = data(:,3:4);
  collisions = data2;
  particles = data3(1);
  
  t0 = data3(3: 3 + particles - 1);
  t3 = [];
  
  finalTime = collisions(end);
  lastThird = 2 * finalTime / 3;
    
  for i = 1:length(collisions)
    start = 3 + (particles + 1) * i;
    if data3(start - 1) >= lastThird
      aux = data3(start: start + particles - 1);
      t3 = [t3;aux];
    end
  end
  
  
endfunction
