package sa_demos;
import Jama.Matrix;

public class FP_dynamics{
	
	Matrix a;
	double freq=Animation.FPS;
	double h=(1./freq); 			// time step
	public double tau=0;
	double deg2rad=Math.PI/180;
	double rad2deg=(180/Math.PI);
	// physical parameters
	double arm1;
	double arm2;
	double m=10;
	final double g=9.81f;
	final double J1=4f;
	Matrix x;
	Matrix x_next;
	Matrix k1,k2,k3,k4;
	
	
	FP_dynamics(double [] x_init, double arm1, double arm2){
		System.out.println(freq);
		double [] x_new={x_init[0]*deg2rad, x_init[1]*deg2rad, x_init[2]*deg2rad,x_init[3]*deg2rad};
		this.x=new Matrix(x_new,1);
		this.arm1=arm1;
		this.arm2=arm2;
	};
	
	
	
	public double [] solve_dyn(){
		k1= f(x,tau).times(h);
		k2= f( x.plus(k1.times(0.5)),tau).times(h);
		k3= f(x.plus(k2.times(0.5)),tau).times(h);
		k4= f( x.plus(k3),tau).times(h);
		x_next= x.plus(k1.times((1.0/6.0))).plus(k2.times((1.0/3.0))).plus(k3.times(1.0/3.0)).plus(k4.times(1.0/6.0));
		//x_next=x.plus(k2);
		x=x_next;
		return new double [] {x.get(0,2)*rad2deg, x.get(0,3)*rad2deg};
	}
	
	public Matrix f(Matrix x, double u){
		Matrix retVal = new Matrix(new double[]{f1(x, u) ,f2(x, u),f3(x, u),f4(x, u)},1);
		return retVal;
	}
	
	public Matrix f(Matrix x){
		Matrix retVal = new Matrix(new double[]{f1(x, 0) ,f2(x, 0),f3(x, 0),f4(x, 0)},1);
		return retVal;
	}
	
	
	public double f1(Matrix x, double u ){
		double x1,x2,x4;
		double tau;
		x1=x.get(0,0); x2=x.get(0,1); x4=x.get(0,3);
		double sign;
		if (x1<0){sign=1;}else{sign=-1;}
		u=u+sign*(2+Math.pow(x1, 2)); // friction and airodynamic damping
		tau=u;
		
		double f_num=(arm1*arm2*m*Math.pow(x1,2)*Math.pow((Math.cos(x4)),2)+(arm1*g*m-2*Math.pow(arm2,2)*m*x1*x2) 
				*(Math.cos(x4))-arm1*arm2*m*Math.pow(x2,2))*(Math.sin(x4))+tau;
		double f_denum=Math.pow(arm2,2)*m*Math.pow((Math.sin(x4)),2)-Math.pow(arm1,2)*m*Math.pow((Math.cos(x4)),2)+Math.pow(arm1,2)*m+J1;
		if(f_denum==0){
			System.out.println("divide by zero");
			return 1.0f;
		}else 
			return f_num/f_denum;
	}
	
	public double f2(Matrix x, double u){
		double x1,x2,x4;
		double tau;
		x1=x.get(0,0); x2=x.get(0,1); x4=x.get(0,3);
		double sign;
		if (x2>0){sign=1;}else{sign=-1;}
		u=u+sign*(1+Math.pow(x2,2));
		tau=u;
		double f_num=(Math.pow(arm2,3)*m*Math.pow(x1,2)*(Math.cos(x4))+Math.pow(arm2,2)*g*m)*Math.pow((Math.sin(x4)),3)+
				(-2*arm1*Math.pow(arm2,2)*m*x1*x2*Math.pow((Math.cos(x4)),2)+((Math.pow(arm1,2)*arm2*m+J1*arm2)*Math.pow(x1,2)-
						Math.pow(arm1,2)*arm2*m*Math.pow(x2,2))*(Math.cos(x4))+Math.pow(arm1,2)*g*m+J1*g)*(Math.sin(x4))+arm1*tau*(Math.cos(x4));
		double f_denum=Math.pow(arm2,3)*m*Math.pow((Math.sin(x4)),2)-Math.pow(arm1,2)*arm2*m*Math.pow((Math.cos(x4)),2)+Math.pow(arm1,2)*arm2*m+J1*arm2;
		if(f_denum==0){
			System.out.println("divide by zero");
			return 1.0f;
		}else
		return f_num/f_denum;
	}
	
	public double f3(Matrix x, double u){
		return x.get(0, 0); 
	}
	
	public double f4(Matrix x, double u){
		return x.get(0,1);
	}

}








































