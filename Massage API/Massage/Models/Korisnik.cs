using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Massage.Models
{
    public class Korisnik
    {
        public long Id { get; set; }
        public String Ime { get; set; }
        public String Lozinka { get; set; }
        public String BrojTelefona { get; set; }

    }
}
