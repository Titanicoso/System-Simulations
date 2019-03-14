function [neighbours, points, data] = parser()
   
  neighbours = dlmread('out.txt', ' ', 0, 1);
  points = dlmread('points.txt', ' ', 1, 0);
  data = dlmread('points.txt', ' ', [0, 0, 0, 2]);
  
endfunction