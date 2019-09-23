using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Massage.Models;
using Massage.ViewModels;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Massage.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AdminController : ControllerBase
    {
        private readonly MojContext _context;

        public AdminController(MojContext context)
        {
            _context = context;
        }
        [HttpGet("{d}/{m}/{g}")]
        public ActionResult<IEnumerable<RezervacijeFull>> GetAll(int d,int m,int g)
        {
            var rezervacije = _context.rezervacije.Where(x => x.Dan == d && x.Mjesec == m && x.Godina == g)
                .Include(x => x.Termin).Include(x => x.User).ToList();
            List<RezervacijeFull> rezultat = new List<RezervacijeFull>();

            foreach(var x in rezervacije)
            {
                RezervacijeFull temp = new RezervacijeFull();
                temp.Termin = x.Termin.Termin;
                temp.Klijent = x.User.Ime;
                temp.BrojTelefona = x.User.BrojTelefona;
                rezultat.Add(temp);
            }
            if(rezultat.Count == 0)
                return null;
            return rezultat;
        }
    }
}