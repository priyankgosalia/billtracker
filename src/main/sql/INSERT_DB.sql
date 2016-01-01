insert into btrack.users (id, username,password,firstname,lastname,enabled,is_admin) 
values (1,'system','system','System','User',0,0);

insert into btrack.users (id, username,password,firstname,lastname,enabled,is_admin) 
values (2,'nikita','b00a50c448238a71ed479f81fa4d9066','Nikita','Nagarkar',1,1);

insert into btrack.users (id, username,password,firstname,lastname,enabled,is_admin) 
values (3,'priyank','fd6c09734988030634a769b0c9b712ee','Priyank','Gosalia',1,0);

insert into btrack.bill_freq (code,description) values ('O','One Time');
insert into btrack.bill_freq (code,description) values ('M','Monthly');
insert into btrack.bill_freq (code,description) values ('Q','Quarterly');
insert into btrack.bill_freq (code,description) values ('H','Half Yearly');
insert into btrack.bill_freq (code,description) values ('Y','Yearly');

commit;

-- single bill master
insert into bill_master(company_id,location,freq_id,amount,payment_mode,user_id,description,due_day,due_date)  values(1,'Goregaon',1,2500.50,'http://cable.com/',1,'Sample Bill',NULL,'2015-12-31');
insert into bill (master_id,amount,status) values (5,2500.50,'N');

insert into bill_master(company_id,location,freq_id,amount,payment_mode,user_id,description,due_day,due_date)  values(2,'Dadar',2,9000,'http://url.com/',1,'Test Bill 2',NULL,'2016-1-15');
insert into bill (master_id,amount,status) values (6,9000,'N');

insert into bill_master(company_id,location,freq_id,amount,payment_mode,user_id,description,due_day,due_date)  values(3,'Borivali',1,380,'RTGS',2,'Test Bill 3',NULL,'2015-12-31');
insert into bill (master_id,amount,status) values (7,380,'N');

insert into bill_master(company_id,location,freq_id,amount,payment_mode,user_id,description,due_day,due_date)  values(4,'Worli',2,764.35,'Bank Transfer',2,'Test Bill 4',NULL,'2016-02-01');
insert into bill (master_id,amount,status) values (8,764,'Y');

commit;


select b.id,bm.id master_id,c.name,b.amount,u.firstName owner,bf.description frequency, bpm.description payment_mode,
bm.description bill_desc, b.status
from btrack.bill_master bm, bill b, bill_freq bf, bill_payment_mode bpm, users u, company c
where b.master_id = bm.id and bm.freq_id = bf.id and bm.payment_mode_id = bpm.id and bm.user_id = u.id
and bm.company_id = c.id;