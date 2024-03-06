select c.name_all AS c_name_all
from items AS i
    join categories AS c on i.category = c.id
where c.name_all like 'Beauty/%%';