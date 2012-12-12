function [ x_solve] = ff(x, tau )
    %UNTITLED Summary of this function goes here
    %   Detailed explanation goes here

    L=[2, 1.5];
    m=40;
    g=9.81;
    J1=1;
    h=1/1450;
    
        
    f1= (sin(x(4))*(cos(x(4))*(L(1)*g-2*x(1)*x(2)*L(2)^2)+x(1)^2*L(1)*L(2)*cos(x(4))^2-L(1)*x(2)^2*L(2))*m+tau)...
        /((L(2)^2*sin(x(4))^2-L(1)^2*cos(x(4))^2+L(1)^2)*m+J1);

    f2=((sin(x(4))^3*(L(2)^2*g+x(1)^2*L(2)^3*cos(x(4)))+sin(x(4))*(L(1)^2*g-2*x(1)*L(1)*x(2)*L(2)^2*cos(x(4))^2+ ...
        (x(1)^2*L(1)^2-L(1)^2*x(2)^2)*L(2)*cos(x(4))))*m+sin(x(4))*(J1*g+x(1)^2*J1*L(2)*cos(x(4)))+L(1)*cos(x(4))*tau)...
    /((L(2)^3*sin(x(4))^2-L(1)^2*L(2)*cos(x(4))^2+L(1)^2*L(2))*m+J1*L(2));



    f3=(x(1));
    f4=(x(2));

    x_solve=[f1, f2, f3,f4]';

end

