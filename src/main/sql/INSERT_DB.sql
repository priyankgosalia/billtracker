insert into btrack.users (id, username,password,firstname,lastname,enabled,is_admin) 
values (1,'system','','System','Reserved User',0,0);

insert into btrack.users (username,password,firstname,lastname,enabled,is_admin) 
values ('nikita','b00a50c448238a71ed479f81fa4d9066','Nikita','Nagarkar',1,1);

insert into btrack.users (username,password,firstname,lastname,enabled,is_admin) 
values ('priyank','fd6c09734988030634a769b0c9b712ee','Priyank','Gosalia',1,0);

insert into btrack.bill_freq (code,description) values ('O','One Time');
insert into btrack.bill_freq (code,description) values ('M','Monthly');
insert into btrack.bill_freq (code,description) values ('Q','Quarterly');
insert into btrack.bill_freq (code,description) values ('H','Half Yearly');
insert into btrack.bill_freq (code,description) values ('Y','Yearly');

commit;


select b.id,bm.id master_id,c.name,b.amount,u.firstName owner,bf.description frequency, bpm.description payment_mode,
bm.description bill_desc, b.status
from btrack.bill_master bm, bill b, bill_freq bf, bill_payment_mode bpm, users u, company c
where b.master_id = bm.id and bm.freq_id = bf.id and bm.payment_mode_id = bpm.id and bm.user_id = u.id
and bm.company_id = c.id;