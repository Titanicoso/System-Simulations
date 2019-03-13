function [neighbours, points, data] = parser()
   
  neighbours = dlmread('out.txt', ' ', 0, 1);
  points = dlmread('data.txt', ' ', 1, 0);
  data = dlmread('data.txt', ' ', [0,0,0, 2]);
  
endfunction