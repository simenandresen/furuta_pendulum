clc; close all; clear all;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55
%
%   Dynamics of furuta pendulum
%
%___________________________________

x=[0.6,0,0.6,0.6]';
end_time=10000;
y=zeros(4,end_time);
tau=0;
h=1/500;


for i=1:end_time
       
    k1=ff(x,tau)*h;
	k2=ff(x+k1*0.5,tau)*h;
	k3=ff(x+k2*0.5,tau)*h;
	k4=ff(x+k3,tau)*h;
	x_next= x+(1/6)*(k1+2*k2+2*k3+k4);
    %x_next=x+h*ff(x,tau);
    y(:,i)=x_next;
    x=x_next;
    
end

%%
close all
plot(1:end_time, y(4,:), '-r', 1:end_time, y(3,:));

ylim([-14,14])



